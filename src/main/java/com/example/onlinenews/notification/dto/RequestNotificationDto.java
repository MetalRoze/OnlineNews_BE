package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.JournalistNotification;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RequestNotificationDto extends BaseNotificationDto {
    private Long reqId;
    private String comment;
    private String sentBy;

    public static RequestNotificationDto fromEntity(JournalistNotification notification) {
        return RequestNotificationDto.builder()
                .id(notification.getId())
                .notificationContent(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .type(notification.getType())
                .reqId(notification.getTargetId())
                .comment(notification.getComment())
                .sentBy(notification.getSenderName())
                .build();
    }

}
