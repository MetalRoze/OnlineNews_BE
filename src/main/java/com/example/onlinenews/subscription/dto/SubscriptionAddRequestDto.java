package com.example.onlinenews.subscription.dto;

import lombok.Data;

@Data
public class SubscriptionAddRequestDto {
    private Long userId;
    private Long publisherId;
}
