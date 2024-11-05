package com.example.onlinenews.subscription.repository;

import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.subscription.entity.Subscription;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
//    List<Subscription> findByUserId(Long userId);
//
//    boolean existsByUserIdAndPublisherId(Long userId, Long publisherId);
//
//    void deleteByUseridAndPublisherId(Long userId, Long publisherId);
}
