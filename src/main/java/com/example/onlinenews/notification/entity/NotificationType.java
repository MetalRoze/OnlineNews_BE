package com.example.onlinenews.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    // 기자 알림
    REQUEST("", "Request"),
    REPORTER_COMMENT("기사에 새로운 댓글이 작성되었습니다.", "Comment"),
    REPORTER_LIKE("기사에 좋아요가 달렸습니다.", "ArticleLike"),
    ENROLL("", "Request"),

    // 사용자 알림 , 일단 나중에
    USER_REPLY("댓글에 대댓글이 작성되었습니다.", null),
    USER_LIKE("댓글에 좋아요가 달렸습니다.", null);

    private final String message;
    private final String targetEntity;

    NotificationType(String message, String targetEntity) {
        this.message = message;
        this.targetEntity = targetEntity;
    }
}
