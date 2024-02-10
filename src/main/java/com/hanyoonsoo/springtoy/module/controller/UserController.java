package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.SingleResponseDto;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.global.security.CustomUserDetails;
import com.hanyoonsoo.springtoy.module.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@Valid @RequestBody UserDto.SignUp signUpDto){
        UserDto.Response reponse = userService.signUp(signUpDto);

        return new ResponseEntity<>(new SingleResponseDto<>(reponse), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getUser(@AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
        UserDto.Response response = userService.userDtoResponse(email);

        System.out.println("getUser: " + user + "=============");
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

}
