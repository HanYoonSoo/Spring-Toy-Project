package com.hanyoonsoo.springtoy.module.repository;

import com.hanyoonsoo.springtoy.module.entity.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
