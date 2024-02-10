package com.hanyoonsoo.springtoy.module.controller;

import com.hanyoonsoo.springtoy.module.dto.LoginDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String loginDto(@Valid @RequestBody LoginDto loginDto){
        return "Success";
    }
}
