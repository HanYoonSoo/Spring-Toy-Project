package com.hanyoonsoo.springtoy.module.dto.order;

import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchDto {

    private String itemName;
    private OrderStatus orderStatus;

}
