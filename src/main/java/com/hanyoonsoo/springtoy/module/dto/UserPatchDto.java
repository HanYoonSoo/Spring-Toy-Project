package com.hanyoonsoo.springtoy.module.dto;

import com.hanyoonsoo.springtoy.module.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UserPatchDto {


    private String nickname;

    private Address address;
}
