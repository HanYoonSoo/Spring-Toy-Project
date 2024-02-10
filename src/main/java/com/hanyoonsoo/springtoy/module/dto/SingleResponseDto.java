package com.hanyoonsoo.springtoy.module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SingleResponseDto<T> {
    private T data;

    public SingleResponseDto(T data) {
        this.data = data;
    }
}
