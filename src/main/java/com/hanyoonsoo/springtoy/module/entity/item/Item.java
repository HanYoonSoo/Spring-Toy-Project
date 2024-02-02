package com.hanyoonsoo.springtoy.module.entity.item;

import com.hanyoonsoo.springtoy.module.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;
    private int stockQuantity;

    //===비즈니스 메소드===//

    /*
        Stock 감소
     */
    public void removeStock(int quantity) {
        int restQuantity = this.stockQuantity - quantity;

        if(restQuantity < 0){
            throw new IllegalStateException("재고가 충분하지 않습니다.");
        }

        this.stockQuantity = restQuantity;
    }

    /*
        Stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

}
