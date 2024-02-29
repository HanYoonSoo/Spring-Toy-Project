package com.hanyoonsoo.springtoy.module.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SingleResponseDto<T> {
    private T data;

    public SingleResponseDto(T data) {
        this.data = data;
    }
}
