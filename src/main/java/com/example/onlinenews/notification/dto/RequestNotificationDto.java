package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestNotificationDto {
    private Long notificationId;
    private String notificationContent;
    private LocalDateTime createdAt;
    private boolean isRead;
    private NotificationType notificationType;

    public RequestNotificationDto(Long notificationId, String notificationContent, LocalDateTime createdAt,boolean isRead, NotificationType notificationType) {
        this.notificationId = notificationId;
        this.notificationContent = notificationContent;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.notificationType = notificationType;
    }
    public static RequestNotificationDto fromEntity(Notification notification){
        return new RequestNotificationDto(
                notification.getId(),
                notification.getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getCreatedAt(),
                notification.isRead(),
                notification.getType()

        );
    }
}
