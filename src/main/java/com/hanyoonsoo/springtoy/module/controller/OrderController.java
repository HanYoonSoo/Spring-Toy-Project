package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.order.OrderDto;
import com.hanyoonsoo.springtoy.module.dto.order.OrderResponseDto;
import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.entity.OrderStatus;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.repository.OrderSearch;
import com.hanyoonsoo.springtoy.module.service.OrderService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity order(@AuthenticationPrincipal CustomUserDetails user, @RequestParam("itemId") Long itemId, @RequestParam("count") int count){
        OrderResponseDto orderResponseDto = orderService.order(user.getEmail(), itemId, count);
        return new ResponseEntity<>(new SingleResponseDto<>(orderResponseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity order(@AuthenticationPrincipal CustomUserDetails user, @Nullable @RequestParam("itemName") String itemName, @Nullable @RequestParam OrderStatus orderStatus){
        List<OrderDto> response = orderService.findSearchOrder(new OrderSearch(user.getEmail(), itemName, orderStatus));
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping("/pages")
    public ResponseEntity orderWithPage(@AuthenticationPrincipal CustomUserDetails user,
                                        @RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit){
        List<OrderDto> response = orderService.findAll(user.getEmail(), offset, limit);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity orderCancel(@PathVariable("orderId") Long orderId){
        String response = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.ACCEPTED);
    }
}
