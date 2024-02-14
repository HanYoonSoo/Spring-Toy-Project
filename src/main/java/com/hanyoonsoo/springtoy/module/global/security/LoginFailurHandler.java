package com.hanyoonsoo.springtoy.module.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanyoonsoo.springtoy.module.dto.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginFailurHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        writePrintErrorResponse(response, exception);
    }

    private void writePrintErrorResponse(HttpServletResponse response, AuthenticationException exception) throws IOException {
        if(exception instanceof BadCredentialsException){
            sendErrorResponse(response, "비밀번호 불일치");
        }
        else if(exception instanceof AccountExpiredException){
            sendErrorResponse(response, "계정만료");
        }
        else if(exception instanceof UsernameNotFoundException){
            sendErrorResponse(response, "계정없음");
        }
        else if(exception instanceof CredentialsExpiredException){
            sendErrorResponse(response, "비밀번호 만료");
        }
        else if(exception instanceof DisabledException){
            sendErrorResponse(response, "계정 비활성화");
        }
        else if(exception instanceof LockedException){
            sendErrorResponse(response, "계정 잠김");
        }
        else{
            sendErrorResponse(response, "확인된 에러가 없습니다.");
        }
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
