package com.hanyoonsoo.springtoy.module.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationResult {

    private boolean authResult;

    @JsonProperty
    private String message;

    private EmailVerificationResult(String message){
        this.message = message;
    }

    public static EmailVerificationResult of(boolean authResult){
        if(authResult){
            return new EmailVerificationResult("이메일 인증에 성공했습니다.");
        }
        else{
            throw new BusinessLogicException(ErrorCode.INVALID_CODE);
        }
    }
}
