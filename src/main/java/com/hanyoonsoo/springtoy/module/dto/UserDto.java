package com.hanyoonsoo.springtoy.module.dto;

import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserDto {

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    private Address address;

    public UserDto(String email, String name, String nickname, Address address) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
    }

    @Getter @Setter
    public static class SignUp extends UserDto{

        @NotBlank
        private String password;

        public SignUp(@Email String email, @NotBlank String password, @NotBlank String name, @NotBlank String nickname, Address address) {
            super(email, name, nickname, address);
            this.password = password;
        }

    }

    public static class Response extends UserDto{

        public Response(User user) {
            super(user.getEmail(), user.getName(), user.getNickName(), user.getAddress());
        }
    }
}
