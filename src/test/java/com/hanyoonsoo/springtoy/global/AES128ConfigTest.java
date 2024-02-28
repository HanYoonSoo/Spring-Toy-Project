package com.hanyoonsoo.springtoy.global;

import com.hanyoonsoo.springtoy.global.BaseIntegrationTest;
import com.hanyoonsoo.springtoy.module.global.config.encryption.AES128Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AES128ConfigTest extends BaseIntegrationTest {

    @Autowired
    private AES128Config aes128Config;

    @Test
    @DisplayName("AES128 암호화 테스트")
    void aes128Test(){
        String text = "this is test";
        String encode = aes128Config.encryptAes(text);
        String decode = aes128Config.decryptAes(encode);

//        log.info("enc = {}", encode);
//        log.info("dec = {}", decode);

        assertThat(decode).isEqualTo(text);
    }
}