package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.UserNotification;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CommentNotificationDto extends BaseNotificationDto {
    private Long targetId;
    private String comment;
    private String sentBy;

    public static CommentNotificationDto fromEntity(JournalistNotification notification) {
        return CommentNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .comment(notification.getComment())
                .sentBy(notification.getSenderName())
                .build();
    }

    public static CommentNotificationDto fromEntity(UserNotification notification) {
        return CommentNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .comment(notification.getReply())
                .sentBy(notification.getSenderName())
                .build();
    }

}
