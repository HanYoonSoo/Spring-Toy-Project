package com.hanyoonsoo.springtoy.module.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 클라이언트에 토큰을 보내기 위한 DTO
 * authorizationType은 JWT에 대한 인증 타입이다.
 */
@Builder
@Data
@AllArgsConstructor
public class TokenDto {

    private String grantType; // JWT에 대한 인증 타입. Bearer를 사용
    private final String authorizationType;
    private String accessToken;
    private String refreshToken;
    private final Long accessTokenExpiresIn;
}
