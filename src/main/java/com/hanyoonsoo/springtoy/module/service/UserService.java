package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.EmailVerificationResult;
import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.EncryptHelper;
import com.hanyoonsoo.springtoy.module.global.config.redis.RedisService;
import com.hanyoonsoo.springtoy.module.global.exception.BusinessLogicException;
import com.hanyoonsoo.springtoy.module.global.exception.ErrorCode;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final UserRepository userRepository;
    private final EncryptHelper encryptHelper;
    private final MailService mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public User findUserAndCheckUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public UserDto.Response signUp(UserDto.SignUp signUpDto) {
        // 가입된 유저인지 확인
        this.checkUserExist(signUpDto.getEmail());

        // 비밀번호 암호화
        String rawPassword = signUpDto.getPassword();
        String encPassword = encryptHelper.encrypt(rawPassword);
        signUpDto.setPassword(encPassword);

        User newUser = User.createUserByDto(signUpDto);

        userRepository.save(newUser);

        return new UserDto.Response(newUser);
    }
    @Transactional
    public void sendCodeToEmail(String toEmail){
        this.checkDuplicatedEmail(toEmail);
        String title = "Test 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장 (key = "AuthCode " + Email / value = AuthCode)
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    @Transactional
    public void sendCodeToEmailForTest(String toEmail, String code){
        this.checkDuplicatedEmail(toEmail);
        String title = "이메일 인증 번호 테스트";
        String authCode = code;
        mailService.sendEmail(toEmail, title, authCode);

        // 이메일 인증 요청 시 인증 번호 Redis에 저장 (key = "AuthCode " + Email / value = AuthCode)
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            if(user.get().isVerify()){
                log.debug("UserService.checkDuplicatedEmail exception occur email: {}", email);
                throw new BusinessLogicException(ErrorCode.VERIFIED_USER);
            }
        }
    }

    private void checkUserExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            log.debug("UserService.checkUserExist exception occur email: {}", email);
            throw new BusinessLogicException(ErrorCode.USER_EXISTS);
        }
    }

    private String createCode(){
        int length = 6;
        try{
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++){
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch(NoSuchAlgorithmException e){
            log.debug("UserService.createCode() exception occur");
            throw new BusinessLogicException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public EmailVerificationResult verifiedCode(String email, String authCode){
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if(authResult){
            updateUserEmailVerified(email);
        }

        return EmailVerificationResult.of(authResult);
    }

    private void updateUserEmailVerified(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            User updateUser = user.get();
            updateUser.setVerify(true);
        }
    }


    public UserDto.Response userDtoResponse(String email) {
        return new UserDto.Response(userRepository.findUserByEmail(email).orElseThrow(NoSuchElementException::new));
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(NoSuchElementException::new);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public UserDto.Response updateUser(String email, UserDto.Patch updateDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
        user.setAddress(updateDto.getAddress());
        user.setNickName(updateDto.getNickname());

        return new UserDto.Response(user);
    }
}
