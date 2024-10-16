package com.example.onlinenews.user.repository;

import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    //이메일 중복 확인
    Optional<User> findByEmail(String user_email);
}
