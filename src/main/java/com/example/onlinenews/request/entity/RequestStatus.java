package com.example.onlinenews.request.entity;

import lombok.Getter;

@Getter
public enum RequestStatus {
    PENDING("승인대기"),
    APPROVED("승인"),
    HOLDING("보류"),
    REJECTED("거절");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }
}
