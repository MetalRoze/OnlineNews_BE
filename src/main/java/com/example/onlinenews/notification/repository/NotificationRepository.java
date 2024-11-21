package com.example.onlinenews.notification.repository;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.type IN (:types)")
    List<Notification> findByUserAndTypes(@Param("user") User user, @Param("types") List<NotificationType> types);

    List<Notification> findByUserAndType(User user, NotificationType type);
    List<Notification> findByUser(User user);
}
