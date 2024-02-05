package com.hanyoonsoo.springtoy.module.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
public enum Authority {

    ROLE_USER("일반사용자"),
    ROLE_ADMIN("일반관리자");

    private final String key;
}
