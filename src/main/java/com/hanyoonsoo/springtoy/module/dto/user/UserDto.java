package com.hanyoonsoo.springtoy.module.dto.user;

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
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class UserDto {

    private String email;

    private String name;

    private String nickname;

    private Address address;

    private Gender gender;


    public UserDto(String email, String name, String nickname, Address address, Gender gender) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.gender = gender;
    }


    @Getter @Setter
    @NoArgsConstructor
    public static class SignUp extends UserDto{

        @NotBlank
        private String password;

        public SignUp(@Email(message = "이메일이 입력되어야 합니다.") String email, @NotBlank(message = "비밀번호가 입력되어야 합니다.") String password,
                      @NotBlank(message = "이름을 입력해주세요.") String name, @NotBlank(message = "닉네임을 입력해주세요.") String nickname,
                      @NotNull(message = "주소를 입력해주세요.") Address address, @NotNull(message = "성별을 선택해야 합니다.") Gender gender) {
            super(email, name, nickname, address, gender);
            this.password = password;
        }

    }

    @NoArgsConstructor
    @Getter @Setter
    public static class Response extends UserDto{

        public Response(User user) {
            super(user.getEmail(), user.getName(), user.getNickName(), user.getAddress(), user.getGender());
        }
    }

//    @Getter @Setter
//    public static class Patch extends UserDto{
//
//        public Patch(@NotBlank(message = "닉네임을 입력해주세요.") String nickname, @NotNull(message = "주소를 입력해주세요.") Address address) {
//            super(nickname, address);
//        }
//
//    }
}
