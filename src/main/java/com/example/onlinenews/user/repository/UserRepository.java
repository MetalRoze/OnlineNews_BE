package com.example.onlinenews.user.repository;

import com.example.onlinenews.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String user_email);

    List<User> findAllByNameAndCp(String username, String cp);
}
