package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.UserNotification;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LikeNotificationDto extends BaseNotificationDto {
    private Long targetId;
    private String sentBy;

    public static LikeNotificationDto fromEntity(JournalistNotification notification) {
        return LikeNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .sentBy(notification.getSenderName())
                .build();
    }
    public static LikeNotificationDto fromEntity(UserNotification notification) {
        return LikeNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .sentBy(notification.getSenderName())
                .build();
    }
}
