package com.hanyoonsoo.springtoy.module.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BusinessLogicExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    protected ResponseEntity<ErrorResponseEntity> handleBusinessLogicException(BusinessLogicException e){
        log.error("BusinessException", e);
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}
