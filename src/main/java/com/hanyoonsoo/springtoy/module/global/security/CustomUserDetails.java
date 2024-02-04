package com.hanyoonsoo.springtoy.module.global.security;

import com.hanyoonsoo.springtoy.module.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CustomUserDetails extends User implements UserDetails {

    private Long id;
    private String email;
    private String role;
    private String password;

    private CustomUserDetails(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().name();
    }

    private CustomUserDetails(String email, String role){
        this.email = email;
        this.role = role;
    }

    private CustomUserDetails(String email, String role, String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public static CustomUserDetails of(User user){
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails of(String email, String role){
        return new CustomUserDetails(email, role);
    }

    public static CustomUserDetails of(String email, String role, String password) {
        return new CustomUserDetails(email, role, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CustomAuthorityUtils.createAuthorities(role);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
