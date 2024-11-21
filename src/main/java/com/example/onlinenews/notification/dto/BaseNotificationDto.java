package com.example.onlinenews.notification.dto;

import com.example.onlinenews.notification.entity.NotificationType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class BaseNotificationDto {
    private Long id;
    private String notificationContent;
    private LocalDateTime createdAt;
    private NotificationType type;
}
