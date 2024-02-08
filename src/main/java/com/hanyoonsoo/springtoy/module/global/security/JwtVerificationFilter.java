package com.hanyoonsoo.springtoy.module.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

/**
 * OncePerRequestFilter는 각 HTTP 요청에 대해 한 번만 실행되는 것을 보장.
 * HTTP 요청마다 JWT를 검증하는 것은 비효율적이기 때문.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    // 인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            List.of("/",
                    "/h2",
                    "/users/signup",
                    "/auth/login",
                    "/auth/reissue");
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    // JWT 인증 정보를 현재 쓰레드의 SecurityContext에 저장(가입/로그인/재발급 Request 제외)
    // HTTP 요청에서 JWT를 꺼내 검증한 후 검증이 되면 SecurityContext에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        try {
            String accessToken = jwtTokenProvider.resolveAccessToken(request);
            System.out.println("access Token: " + accessToken);
            if (StringUtils.hasText(accessToken) && doNotLogout(accessToken)
                    && jwtTokenProvider.validateToken(accessToken, response)) {
                setAuthenticationToContext(accessToken);
            }
            // TODO: 예외처리 리팩토링
        } catch (RuntimeException e) {
            if (e instanceof RuntimeException) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(new IllegalStateException());
                response.getWriter().write(json);
                response.setStatus(403);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean doNotLogout(String accessToken) {
        String isLogout = redisService.getValues(accessToken);
        return isLogout.equals("false");
    }

    // EXCLUDE_URL과 동일한 요청이 들어왔을 경우, 현재 필터를 진행하지 않고 다음 필터 진행
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        boolean result = EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));

        return result;
    }

    private void setAuthenticationToContext(String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("# Token verification success!");
    }
}