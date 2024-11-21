package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class JournalNotificationDto extends BaseNotificationDto {
    private Long id;
    private String comment;
    private String sentBy;

    public static JournalNotificationDto fromEntity(JournalistNotification notification) {
        return JournalNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .id(notification.getTargetId())
                .comment(notification.getComment())
                .sentBy(notification.getSenderName())
                .build();
    }

}
