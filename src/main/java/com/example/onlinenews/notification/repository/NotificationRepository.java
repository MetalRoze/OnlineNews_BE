package com.example.onlinenews.notification.repository;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findNotificationsByUserAndType(User user, NotificationType notificationType);
    List<Notification> findByUser(User user);
}
