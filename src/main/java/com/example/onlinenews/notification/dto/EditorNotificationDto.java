package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.EditorNotification;
import com.example.onlinenews.user.entity.UserGrade;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditorNotificationDto {
    private Long id;
    private Long requestId;
    private String sentBy;
    private UserGrade userGrade;
    private String notificationContent;
    private LocalDateTime createdAt;

    public EditorNotificationDto(Long id, Long requestId,  String sentBy, UserGrade userGrade, String notificationContent, LocalDateTime createdAt) {
        this.id = id;
        this.requestId=requestId;
        this.sentBy = sentBy;
        this.userGrade = userGrade;
        this.notificationContent = notificationContent;
        this.createdAt = createdAt;
    }

    public static EditorNotificationDto fromEntity(EditorNotification editorNotification){
        return new EditorNotificationDto(
                editorNotification.getId(),
                editorNotification.getRequest().getId(),
                editorNotification.getRequest().getUser().getName(),
                editorNotification.getRequest().getUser().getGrade(),
                editorNotification.getRequest().getArticle().getTitle()+" "+editorNotification.getType().getMessage(),
                editorNotification.getCreatedAt()
        );
    }

}
