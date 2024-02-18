package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.LoginResponse;
import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AES128Config aes128Config;
    private final RedisService redisService;
    private final UserRepository userRepository;

    public void logout(String encryptedRefreshToken, String accessToken) {
        this.verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValues(email);
        if(redisService.checkExistsValue(redisRefreshToken)){
            redisService.deleteValues(email);

            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout")
            long accessTokenValidityInSeconds = jwtTokenProvider.getAccessTokenValidityInSeconds();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenValidityInSeconds));
        }
    }

    public LoginResponse login(LoginDto loginDto) {
        return new LoginResponse(loginDto.getEmail(), "test");
    }

    private void verifiedRefreshToken(String encryptedRefreshToken){
        if(encryptedRefreshToken == null){
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }
    }

    private User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
    }

    public String reissueAccessToken(String encryptedRefreshToken) {
        this.verifiedRefreshToken(encryptedRefreshToken);
        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValues(email);

        if(redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)){
            User findUser = this.findUserByEmail(email);
            CustomUserDetails userDetails = CustomUserDetails.of(findUser);
            TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
            String newAccessToken = tokenDto.getAccessToken();
//            long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenValidityInSeconds();
//            redisService.setValues(email, redisRefreshToken, Duration.ofMillis(refreshTokenExpirationMillis));

            return newAccessToken;
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }


}
