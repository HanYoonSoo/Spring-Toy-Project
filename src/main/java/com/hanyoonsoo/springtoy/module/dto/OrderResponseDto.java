package com.hanyoonsoo.springtoy.module.dto;

import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.Order;
import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderResponseDto {

    private String nickName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderResponseDto(Order order){
        this.nickName = order.getUser().getNickName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        this.address = order.getDelivery().getAddress();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }
}
