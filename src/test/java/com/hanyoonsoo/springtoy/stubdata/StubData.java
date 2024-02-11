package com.hanyoonsoo.springtoy.stubdata;

import com.hanyoonsoo.springtoy.module.constants.Authority;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.Address;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StubData {

    public static class MockUser extends StubData{


        public static UserDto.SignUp getSignUpDto() {
            return new UserDto.SignUp("email@gmail.com", "1234", "test", "test", new Address("test", "1234", "1234"));
        }

        public static CustomUserDetails getUserDetails() {
            return CustomUserDetails.of(new User());
        }
    }
}