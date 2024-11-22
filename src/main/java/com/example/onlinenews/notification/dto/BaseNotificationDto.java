package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public abstract class BaseNotificationDto {
    private Long id;
    private String notificationContent;
    private LocalDateTime createdAt;
    private NotificationType type;

}
