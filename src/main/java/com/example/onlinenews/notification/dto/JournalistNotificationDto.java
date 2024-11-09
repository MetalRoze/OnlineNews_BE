package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalistNotificationDto {
    private Long id;
    private String notificationContent;
    private String comment;
    private LocalDateTime createdAt;

    public JournalistNotificationDto(Long id, String notificationContent, String comment, LocalDateTime createdAt) {
        this.id = id;
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


