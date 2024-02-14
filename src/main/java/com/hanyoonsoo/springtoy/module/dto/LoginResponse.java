package com.hanyoonsoo.springtoy.module.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.connector.Response;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LoginResponse{

    private String email;
    private String nickName;
}
