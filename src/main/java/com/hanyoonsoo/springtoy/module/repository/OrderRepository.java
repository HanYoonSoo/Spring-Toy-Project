package com.hanyoonsoo.springtoy.module.repository;

import com.hanyoonsoo.springtoy.module.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustom {
}
