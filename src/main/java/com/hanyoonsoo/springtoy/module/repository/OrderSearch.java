package com.hanyoonsoo.springtoy.module.repository;

import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class OrderSearch {

    private String email;
    private String itemName;
    private OrderStatus orderStatus;
}
