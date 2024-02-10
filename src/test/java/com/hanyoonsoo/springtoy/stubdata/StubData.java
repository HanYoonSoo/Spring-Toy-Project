package com.hanyoonsoo.springtoy.stubdata;

import com.hanyoonsoo.springtoy.module.constants.Authority;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;

public class StubData {

    public static class MockUser {

        public static UserDto.SignUp getSignUpDto() {
            return new UserDto.SignUp("email@gmail.com", "1234", "test", "test", new Address("test", "1234", "1234"));
        }

        public static CustomUserDetails getUserDetails() {
            UserDto.SignUp signUpDto = getSignUpDto();
            User user = new User();
            user.setEmail(signUpDto.getEmail());
            user.setRole(Authority.ROLE_USER);
            return CustomUserDetails.of(user);
        }
    }
}
