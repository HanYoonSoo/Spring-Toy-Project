package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.LoginResponse;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity loginDto(@Valid @RequestBody LoginDto loginDto){
        return new ResponseEntity<>(new SingleResponseDto<>(authService.login(loginDto)), HttpStatus.OK);
    }

    @PatchMapping ("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        authService.logout(encryptedRefreshToken, accessToken);

        return new ResponseEntity<>(new SingleResponseDto<>("Logged out successfully"), HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/reissue")
    public ResponseEntity reissue(HttpServletRequest request, HttpServletResponse response){
        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
        String newAccessToken = authService.reissueAccessToken(encryptedRefreshToken);
        jwtTokenProvider.accessTokenSetHeader(newAccessToken, response);

        return new ResponseEntity<>(new SingleResponseDto<>("Access Token reissued"), HttpStatus.OK);
    }
}
