package com.hanyoonsoo.springtoy.module.dto.order;

import com.hanyoonsoo.springtoy.module.entity.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class OrderDto {

    private Long orderId;
    private String nickName;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order){
        orderId = order.getId();
        nickName = order.getUser().getNickName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
        order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(toList());

    }

}
