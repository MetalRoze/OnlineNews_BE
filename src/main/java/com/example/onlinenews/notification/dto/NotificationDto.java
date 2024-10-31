package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long notificationId;
    private String userName;
    private String articleTitle;
    private LocalDateTime createdAt;
    private boolean isRead;

    public NotificationDto(Long notificationId, String userName, String articleTitle, LocalDateTime createdAt,boolean isRead) {
        this.notificationId = notificationId;
        this.userName = userName;
        this.articleTitle = articleTitle;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }
    public static NotificationDto fromEntity(Notification notification){
        return new NotificationDto(
                notification.getId(),
                notification.getUser().getName(),
                notification.getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}
