package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalistNotificationDto {
    private Long notificationId;
    private String notificationContent;
    private String comment;
    private LocalDateTime createdAt;

    public JournalistNotificationDto(Long notificationId, String notificationContent, String comment, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.notificationContent = notificationContent;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public static JournalistNotificationDto fromEntity(JournalistNotification notification){
        return new JournalistNotificationDto(
                notification.getId(),
                notification.getRequest().getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getRequest().getComment(),
                notification.getCreatedAt()
        );
    }
}


