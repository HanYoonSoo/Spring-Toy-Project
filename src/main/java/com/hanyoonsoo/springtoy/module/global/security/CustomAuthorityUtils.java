package com.hanyoonsoo.springtoy.module.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hanyoonsoo.springtoy.module.constants.Authority.ADMIN;
import static com.hanyoonsoo.springtoy.module.constants.Authority.USER;

@Slf4j
public class CustomAuthorityUtils {

    public static List<GrantedAuthority> createAuthorities(String role){
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public static void verifiedRole(String role){
        if(role == null){
            throw new IllegalStateException("역할이 없습니다.");
        } else if(!role.equals(USER.name()) && !role.equals(ADMIN.name())){
            throw new IllegalStateException("맞는 역할이 없습니다.");
        }
    }
}
