package com.hanyoonsoo.springtoy.module.repository;


import com.hanyoonsoo.springtoy.module.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findById(Long id);

    public Optional<User> findByEmail(String email);

    public Optional<User> findUserByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isVerify = true WHERE u.email = :email")
    void updateVerified(@Param("email") String email);
}
