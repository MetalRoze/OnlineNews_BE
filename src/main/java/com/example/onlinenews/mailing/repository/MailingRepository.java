package com.example.onlinenews.mailing.repository;

import com.example.onlinenews.mailing.entity.Mailing;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailingRepository extends JpaRepository<Mailing, Long> {
    Mailing findByUser(User user);

    boolean existsByUser(User user);
}
