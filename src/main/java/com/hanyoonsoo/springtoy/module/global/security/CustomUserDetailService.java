package com.hanyoonsoo.springtoy.module.global.security;

import com.hanyoonsoo.springtoy.module.entity.User;
import com.hanyoonsoo.springtoy.module.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * loadByUsername() : 사용자 이름(email)을 입력받아 User에서 사용자 정보를 조회한다.
     * 조회한 User 객체가 존재하면 createUserDetails() 메서드를 사용해서 CusotmUserDetails 객체를 생성하고 반환한다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(NoSuchElementException::new);
    }

    private UserDetails createUserDetails(User user){
        return CustomUserDetails.of(user);
    }
}
