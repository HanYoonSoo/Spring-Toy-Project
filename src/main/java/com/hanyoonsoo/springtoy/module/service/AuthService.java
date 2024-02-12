package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.TokenDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.AES128Config;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.global.security.JwtTokenProvider;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.NoSuchElementException;

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

    private void verifiedRefreshToken(String encryptedRefreshToken){
        if(encryptedRefreshToken == null){
            throw new IllegalStateException("refresh Token이 없습니다.");
        }
    }

    private User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
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
        else throw new IllegalStateException("리프레시 토큰이 맞지 않습니다.");

    }
}
