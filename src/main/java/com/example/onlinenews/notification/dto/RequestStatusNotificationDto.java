package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.request.entity.Request;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestStatusNotificationDto {
    private Long notificationId;
    private String notificationContent;
    private String comment;
    private LocalDateTime createdAt;
    private boolean isRead;
    private NotificationType notificationType;

    @Builder
    public RequestStatusNotificationDto(Long notificationId, String notificationContent, String comment, LocalDateTime createdAt, boolean isRead, NotificationType notificationType) {
        this.notificationId = notificationId;
        this.notificationContent = notificationContent;
        this.comment = comment;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.notificationType = notificationType;
    }


    public static RequestStatusNotificationDto fromEntity(Notification notification, Request request){
        return new RequestStatusNotificationDto(
                notification.getId(),
                request.getComment(),
                notification.getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getCreatedAt(),
                notification.isRead(),
                notification.getType()
        );
    }
}


