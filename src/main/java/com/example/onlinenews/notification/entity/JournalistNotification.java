package com.example.onlinenews.notification.entity;


import com.example.onlinenews.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;


@Entity
@Getter
@DiscriminatorValue("JournalistNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class JournalistNotification extends Notification {
    private Long targetId;
    private String senderName; //알림 보내는 사람
    private String comment;

    public JournalistNotification(User user, NotificationType type, String message, boolean isRead, LocalDateTime createdAt,  Long targetId, String senderName, String comment) {
        super(user, type, createdAt, message, isRead);
        this.targetId = targetId;
        this.senderName = senderName;
        this.comment = comment;
    }
}

