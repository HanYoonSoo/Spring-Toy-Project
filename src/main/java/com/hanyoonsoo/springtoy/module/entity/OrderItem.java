package com.hanyoonsoo.springtoy.module.entity;

import com.hanyoonsoo.springtoy.module.entity.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter @Setter
@SQLDelete(sql = "UPDATE orderitem SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;
    private int count;

    private boolean deleted = Boolean.FALSE;

    //===생성 메소드===//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }

    //===비즈니스 메소드===//
    public void cancel() {
        getItem().addStock(getCount());
    }

    //===조회 메소드===//
    public int totalPrice(){
        return getOrderPrice() * getCount();
    }
}
