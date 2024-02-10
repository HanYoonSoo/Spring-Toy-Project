package com.hanyoonsoo.springtoy.module.global.config;

public interface EncryptHelper {

    String encrypt(String password);    // 암호화
    boolean isMatch(String password, String hashed); // 해시된 함수와 비교해서 맞는지 비교
}
