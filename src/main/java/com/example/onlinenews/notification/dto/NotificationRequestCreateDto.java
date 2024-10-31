package com.example.onlinenews.notification.dto;

import lombok.Getter;

@Getter
public class NotificationRequestCreateDto {
    private Long userId;
    private Long articleId;
}
