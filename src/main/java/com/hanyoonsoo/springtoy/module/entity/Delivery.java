package com.hanyoonsoo.springtoy.module.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter @Setter
@SQLDelete(sql = "UPDATE delivery SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Delivery extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery")
    private Order order;

    private boolean deleted = Boolean.FALSE;
}
