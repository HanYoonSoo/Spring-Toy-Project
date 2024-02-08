package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.dto.UserDto;
import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.global.config.EncryptHelper;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.util.annotation.NonNullApi;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EncryptHelper encryptHelper;

    public User findUserAndCheckUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public UserDto.Response signUp(UserDto.SignUp signUpDto) {
        // 비밀번호 암호화
        String rawPassword = signUpDto.getPassword();
        String encPassword = encryptHelper.encrypt(rawPassword);
        signUpDto.setPassword(encPassword);

        User newUser = User.createUserByDto(signUpDto);

        userRepository.save(newUser);

        return new UserDto.Response(newUser);
    }

    public UserDto.Response findMemberByEmail(String email) {
        return new UserDto.Response(userRepository.findUserByEmail(email).orElseThrow(NoSuchElementException::new));
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
