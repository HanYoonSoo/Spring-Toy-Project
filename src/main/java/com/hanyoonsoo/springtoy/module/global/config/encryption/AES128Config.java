package com.hanyoonsoo.springtoy.module.global.config.encryption;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 암호화는 저장중이거나 전송하려는 데이터를 보호하는 데 사용되는 대칭 암호화 알고리즘이다.
 * 고정된 크기의 데이터 블록에 데이터를 암호화하는 블록 암호 암호화 알고리즘.
 * AES 암호화는 데이터의 발신자와 수신자 간 공유되는 SecretKey 사용 <- 일반 텍스트 암호화 및 복호화하는데 사용
 * AES 암호화는 SecretKey 길이에 따라 종류가 다른데 128, 192, 256bit 지원
 */
@Component
//@Profile("!test")
public class AES128Config {
    private static final Charset ENCODING_TYPE = StandardCharsets.UTF_8;
    private static final String INSTANCE_TYPE = "AES/CBC/PKCS5Padding";

    @Value("${aes.secret-key}")
    private String secretKey;
    private IvParameterSpec ivParameterSpec; // CBC 모드의 IV를 만들기 위해 사용
    private SecretKeySpec secretKeySpec;    // 비밀키를 만드는데 사용됨
    private Cipher cipher; // AES 및 다양한 암호화 알고리즘을 사용하여 데이터를 암호화 및 해독하는 메서드 제공

    /**
     * SecureRandom 인스턴스를 사용해서 16바이트 크기의 난수 바이트 배열 생성
     * 해당 바이트 배열을 사용해서 IvParameterSpec 객체를 생성
     * SecurityRandom 사용 이유: SecretKeySpec과 IvParameterSpec에 동일한 키를 사용하게 되면 보안에 취약하기 때문
     * 따라서, 난수 생성기인 SecurityRandom 사용
     */
    @PostConstruct
    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException{
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // 128bits
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);

        /*
         * ENCODING_TYPE(UTF-8)를 사용하여 SecretKey 문자열을 바이트로 변환한 키 값과 문자열 "AES"를 사용하여
         * SecretKeySpec를 생성
         */
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(ENCODING_TYPE), "AES");
        cipher = Cipher.getInstance(INSTANCE_TYPE); // 알고리즘 타입을 지정하여 Cipher 생성. 위 코드에서는 AES/CBC/PKCS5Padding 사용
    }

    // AES 암호화
    public String encryptAes(String plaintext){
        try{
            // 암호화 모드에서 Cipher를 초기화하고 doFinal() 메소드로 데이터 암호화
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(ENCODING_TYPE));
            return new String(Base64.getEncoder().encode(encrypted), ENCODING_TYPE);
        } catch (Exception e){
            throw new IllegalStateException("ENCRYPTION_FAILED");
        }
    }

    // AES 복호화
    public String decryptAes(String plaintext) {
        try {
            // 복호화 모드에서 Cipher를 초기화하고 doFinal() 메소드로 데이터 복호화
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decoded = Base64.getDecoder().decode(plaintext.getBytes(ENCODING_TYPE));
            return new String(cipher.doFinal(decoded), ENCODING_TYPE); // 복호화한 데이터도 문자열로 인코딩하여 반환
        } catch (Exception e) {
            throw new IllegalStateException("DECRYPTION_FAILED");
        }
    }
}
