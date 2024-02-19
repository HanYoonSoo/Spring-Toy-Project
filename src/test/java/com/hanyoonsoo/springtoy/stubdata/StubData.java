package com.hanyoonsoo.springtoy.stubdata;

import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import com.hanyoonsoo.springtoy.module.dto.LoginResponse;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.dto.UserPatchDto;
import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.Gender;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class StubData {

    public static class MockUser extends StubData{


        public static UserDto.SignUp getSignUpDto() {
            return new UserDto.SignUp("email@gmail.com", "1234", "test", "test", new Address("test", "1234", "1234"), Gender.from("남자"));
        }

        public static CustomUserDetails getUserDetails() {
            return CustomUserDetails.of(new User());
        }

        public static LoginDto getLoginSuccessDto() {
            return new LoginDto("email@gmail.com", "1234");
        }

        public static LoginResponse getLoginResponseDto() {
            return new LoginResponse("email@gmail.com", "test");
        }

        public static LoginDto getLoginFailDto() {
            return new LoginDto("email@gmail.com", "12345");
        }

        public static UserPatchDto getPatchDto() {
            return new UserPatchDto("test2", new Address("test2", "12345", "12345"));
        }
    }
}
