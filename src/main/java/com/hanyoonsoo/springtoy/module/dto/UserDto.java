package com.hanyoonsoo.springtoy.module.dto;

import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.Gender;
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

    @Email(message = "이메일이 입력되어야 합니다.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotNull(message = "주소를 입력해주세요.")
    private Address address;

    @NotNull(message = "성별을 선택해야 합니다.")
    private Gender gender;

    public UserDto(String email, String name, String nickname, Address address, Gender gender) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.gender = gender;
    }

    @Getter @Setter
    public static class SignUp extends UserDto{

        @NotBlank
        private String password;

        public SignUp(@Email String email, @NotBlank String password, @NotBlank String name, @NotBlank String nickname, Address address, Gender gender) {
            super(email, name, nickname, address, gender);
            this.password = password;
        }

    }

    public static class Response extends UserDto{

        public Response(User user) {
            super(user.getEmail(), user.getName(), user.getNickName(), user.getAddress(), user.getGender());
        }
    }
}
