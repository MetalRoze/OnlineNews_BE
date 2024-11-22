package com.example.onlinenews.notification.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("UserNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
public class UserNotification extends Notification {
    private Long targetId;
    private String senderName;
    private String reply;

    public UserNotification(User user, NotificationType type, String message, boolean isRead, LocalDateTime createdAt, Long targetId, String senderName, String reply) {
        super(user, type, createdAt, message, isRead);
        this.targetId = targetId;
        this.senderName = senderName;
        this.reply = reply;
    }
}
