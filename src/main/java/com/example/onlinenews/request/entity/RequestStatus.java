package com.example.onlinenews.request.entity;

import lombok.Getter;

@Getter
public enum RequestStatus {
    PENDING("승인 대기"),
    APPROVED("승인됨"),
    HOLDING("보류됨"),
    REJECTED("거절됨");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }
}
