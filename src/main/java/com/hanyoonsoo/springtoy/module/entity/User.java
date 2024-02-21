package com.hanyoonsoo.springtoy.module.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanyoonsoo.springtoy.module.constants.Authority;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private boolean isVerify = Boolean.FALSE;

    private boolean deleted = Boolean.FALSE;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Image> images = new ArrayList<>();


    //==DTO 활용 유저 생성 메서드==//
    public static User createUserByDto(UserDto.SignUp signUpDto) {
        User user = new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setNickName(signUpDto.getNickname());
        user.setRole(Authority.ROLE_USER);
        user.setAddress(signUpDto.getAddress());
        user.setPassword(signUpDto.getPassword());
        user.setGender(signUpDto.getGender());
        return user;
    }
}
