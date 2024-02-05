package com.hanyoonsoo.springtoy.module.service;

import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserAndCheckUserExists(Long id) {
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
