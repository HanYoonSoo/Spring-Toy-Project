package com.hanyoonsoo.springtoy.module.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;

/**
 * 로그인 검증 및 JWT 발급 담당
 * UsernamePasswordAuthenticationFilter == Spring Security에서 사용자 이름과 암호를 받아 인증 시도
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RedisService redisService;
    private final AES128Config aes128Config;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // ServletInputStream을 LoginDto에 담기 위해 역직렬화 수행
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());


        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal(); // CustomUserDetails 클래스의 객체를 얻음
        TokenDto tokenDto = jwtTokenProvider.generateToken(customUserDetails);
        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();
        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);
        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        jwtTokenProvider.refreshTokenSetHeader(encryptedRefreshToken, response);
        User findUser = userService.findUserAndCheckUserExists(customUserDetails.getId());
//        Responder.loginSuccessResponse(response, findUser)

        // 로그인 성공시 Refresh Token Redis 저장(key = Email / value = Refresh Token)
        long refreshTokenValidityInSeconds = jwtTokenProvider.getRefreshTokenValidityInSeconds();
        redisService.setValues(findUser.getEmail(), refreshToken, Duration.ofMillis(refreshTokenValidityInSeconds));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
