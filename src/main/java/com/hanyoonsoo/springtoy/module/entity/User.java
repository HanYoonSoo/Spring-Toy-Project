package com.hanyoonsoo.springtoy.module.entity;

import com.hanyoonsoo.springtoy.module.constants.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Table(name = "users")
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private Authority role;

    private String nickName;

    @Embedded
    private Address address;

    private boolean isVerify = Boolean.FALSE;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Image> images = new ArrayList<>();
}
