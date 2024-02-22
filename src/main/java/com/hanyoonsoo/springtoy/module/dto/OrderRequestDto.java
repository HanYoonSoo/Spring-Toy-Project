package com.hanyoonsoo.springtoy.module.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequestDto {

    private Long itemId;
    private int count;
}
