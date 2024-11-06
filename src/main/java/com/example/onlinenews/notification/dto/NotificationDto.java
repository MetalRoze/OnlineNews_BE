package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.user.entity.UserGrade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long notificationId;
    private String userName;
    private UserGrade grade;
    private String articleTitle;
    private LocalDateTime createdAt;
    private boolean isRead;

    public NotificationDto(Long notificationId, String userName, UserGrade grade, String articleTitle, LocalDateTime createdAt,boolean isRead) {
        this.notificationId = notificationId;
        this.userName = userName;
        this.grade = grade;
        this.articleTitle = articleTitle;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }
    public static NotificationDto fromEntity(Notification notification){
        return new NotificationDto(
                notification.getId(),
                notification.getUser().getName(),
                notification.getUser().getGrade(),
                notification.getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}
