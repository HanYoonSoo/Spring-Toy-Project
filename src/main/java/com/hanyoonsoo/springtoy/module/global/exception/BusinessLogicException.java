package com.hanyoonsoo.springtoy.module.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessLogicException extends RuntimeException{
    ErrorCode errorCode;
}
