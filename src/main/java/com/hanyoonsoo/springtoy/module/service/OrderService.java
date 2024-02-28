package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.order.OrderDto;
import com.hanyoonsoo.springtoy.module.dto.order.OrderResponseDto;
import com.hanyoonsoo.springtoy.module.entity.Delivery;
import com.hanyoonsoo.springtoy.module.entity.Order;
import com.hanyoonsoo.springtoy.module.entity.OrderItem;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.entity.item.Item;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.repository.ItemRepository;
import com.hanyoonsoo.springtoy.module.repository.OrderRepository;
import com.hanyoonsoo.springtoy.module.repository.OrderSearch;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public OrderResponseDto order(String email, Long itemId, int count){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new BusinessLogicException(ErrorCode.ITEM_NOT_FOUND));

        Delivery delivery = new Delivery();
        delivery.setAddress(user.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(user, delivery, orderItem);

        orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    public List<OrderDto> findSearchOrder(OrderSearch orderSearch) {
        List<Order> orders  = orderRepository.findAllWithItem(orderSearch);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }

    public List<OrderDto> findAll(String email, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<Order> orders = orderRepository.findAllWithItemPageable(pageable, email);

        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }

    @Transactional
    public String cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BusinessLogicException(ErrorCode.ORDER_NOT_FOUND));

        //주문 취소
        order.cancel();

        return "주문을 취소했습니다.";
    }
}
