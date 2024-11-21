package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class RequestNotificationDto extends BaseNotificationDto {
    private String comment;
    private String sentBy;

    public RequestNotificationDto(Long id, String sentBy, String notificationContent, String comment, LocalDateTime createdAt, NotificationType type) {
        super(id, notificationContent, createdAt, type);
        this.comment = comment;
        this.sentBy = sentBy;
    }

    public static RequestNotificationDto fromEntity(JournalistNotification notification) {
        return new RequestNotificationDto(
                notification.getId(),
                notification.getRequest().getPublisher().getName(),
                notification.getRequest().getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getRequest().getComment(),
                notification.getCreatedAt(),
                notification.getType()
        );
    }
}


