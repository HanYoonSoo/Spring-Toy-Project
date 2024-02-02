package com.hanyoonsoo.springtoy.module.entity;

import com.hanyoonsoo.springtoy.module.constants.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
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

    private Boolean isVerify;

}
