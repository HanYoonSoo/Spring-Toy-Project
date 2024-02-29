package com.hanyoonsoo.springtoy.module.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanyoonsoo.springtoy.module.dto.auth.Response;
import com.hanyoonsoo.springtoy.module.dto.auth.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

/**
 * JWT 토큰을 생성, 토큰 복호화 및 정보 추출, 토큰 유효성 검증 클래스
 * jwt.secret는 토큰의 암호화, 복호화를 위한 secret key로 HS256 알고리즘 사용을 위해, 256비트보다 커야함.
 * 한 단어에 1바이트이므로, 32글자 이상 설정.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    public static final String BEARER = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.accessToken-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Getter
    @Value("${jwt.refreshToken-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private Key key;

    @PostConstruct
    public void keyInit(){
        String base64EncodedSecretKey = Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityInSeconds *= 1000;
        this.refreshTokenValidityInSeconds *= 1000;
    }

    // 유저 정보를 토대로 AccessToken, RefreshToken 생성
    public TokenDto generateToken(CustomUserDetails customUserDetails){
        // 권한 가져오기
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, customUserDetails.getRole_str());

        Date accessValidity = getTokenValidityInSeconds(accessTokenValidityInSeconds);
        Date refreshValidity = getTokenValidityInSeconds(refreshTokenValidityInSeconds);

        // AccessToken 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(customUserDetails.getUsername())
                .setExpiration(accessValidity)
                .setIssuedAt(Calendar.getInstance().getTime())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(refreshValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER)
                .authorizationType(AUTHORIZATION_HEADER)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenValidityInSeconds)
                .build();
    }

    public Date getTokenValidityInSeconds(long tokenValidityInSeconds) {
        long now = (new Date()).getTime();
        return new Date(now + tokenValidityInSeconds);
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼냄
    public Authentication getAuthentication(String accessToken){
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if(claims.get(AUTHORITIES_KEY) == null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String authority = claims.get(AUTHORITIES_KEY).toString();


        CustomUserDetails customUserDetails = CustomUserDetails.of(
                claims.getSubject(), authority);


        log.info("# AuthUser.getRoles 권한 체크 = {}", customUserDetails.getAuthorities().toString());

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

    }

    // 토큰 정보 검증
    public boolean validateToken(String token, HttpServletResponse response) throws IOException {
        try{
            parseClaims(token);
        } catch(MalformedJwtException e){
            log.info("Invalid JWT token");
            log.trace("Invalid JWT token trace = {}", e);
            sendErrorResponse(response, "손상된 토큰입니다.");
        } catch (ExpiredJwtException e){
            log.info("Expired JWT token");
            log.trace("Expired JWT token trace = {}", e);
            sendErrorResponse(response, "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token");
            log.trace("Unsupported JWT token trace = {}", e);
            sendErrorResponse(response, "지원하지 않는 토큰입니다.");
        } catch(IllegalArgumentException e){
            log.info("JWT claims string is empty");
            log.trace("JWT claims string is empty trace = {}", e);
            sendErrorResponse(response, "시그니처 검증에 실패한 토큰입니다.");
        } catch(BadCredentialsException e){
            log.info("Login Info Error");
            log.trace("Login Info Error is empty trace = {}", e);
            sendErrorResponse(response, "로그인 정보가 잘못되었습니다.");
        }
        return true;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void accessTokenSetHeader(String accessToken, HttpServletResponse response){
        String headerValue = BEARER_PREFIX + accessToken;
        response.setHeader(AUTHORIZATION_HEADER, headerValue);
    }

    public void refreshTokenSetHeader(String refreshToken, HttpServletResponse response){
        response.setHeader("Refresh", refreshToken);
    }

    // Request Header의 Access Token 정보 추출
    public String resolveAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }

        return null;
    }

    // Request Header에 Refresh Token 정보 추출
    public String resolveRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if(StringUtils.hasText(refreshToken)){
            return refreshToken;
        }

        return null;
    }

    // JWT 예외처리
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new Response(HttpStatus.UNAUTHORIZED.value(), message)));
    }
}
