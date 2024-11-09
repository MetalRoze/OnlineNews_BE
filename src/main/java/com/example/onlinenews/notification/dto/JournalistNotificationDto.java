package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalistNotificationDto {
    private Long id;
    private String notificationContent;
    private String comment;
    private LocalDateTime createdAt;
    private String sentBy;

    public JournalistNotificationDto(Long id, String sentBy, String notificationContent, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.sentBy = sentBy;
        this.notificationContent = notificationContent;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public static JournalistNotificationDto fromEntity(JournalistNotification notification){
        NotificationType notificationType = notification.getType();
        String sentBy="";
        if(notificationType.getMessage().equals("REPORTER_LIKE")){
            sentBy= notification.getArticleLike().getUser().getName();
        }
        return new JournalistNotificationDto(
                notification.getId(),
                sentBy,
                notification.getRequest().getArticle().getTitle() + " " + notification.getType().getMessage(),
                notification.getRequest().getComment(),
                notification.getCreatedAt()
        );
    }
}


