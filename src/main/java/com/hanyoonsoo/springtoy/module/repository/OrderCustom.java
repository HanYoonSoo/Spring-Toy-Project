package com.hanyoonsoo.springtoy.module.repository;

import com.hanyoonsoo.springtoy.module.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderCustom {

    public List<Order> findAllWithItem(OrderSearch orderSearch);

    List<Order> findAllWithItemPageable(Pageable pageable, String email);
}
