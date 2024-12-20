package com.example.onlinenews.subscription.repository;

import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.subscription.entity.Subscription;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(Long userId);
    Optional<Subscription> findByPublisherIdAndUser(Long publisherId, User user);
    boolean existsByUserAndPublisher(User user,Publisher publisher);

}
