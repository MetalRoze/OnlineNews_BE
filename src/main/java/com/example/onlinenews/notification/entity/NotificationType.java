package com.example.onlinenews.notification.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    //기자
    REPORTER_APPROVAL_ACCEPTED("승인 요청이 수락되었습니다."),
    REPORTER_APPROVAL_HELD("승인 요청이 보류되었습니다."),
    REPORTER_APPROVAL_REJECTED("승인 요청이 거절되었습니다."),
    REPORTER_COMMENT("기사에 새로운 댓글이 작성되었습니다."),
    //사용자
    USER("새로운 알림이 있습니다."), //사용자는 뭔 알람 필요하죠
    USER_REPLY("댓글에 대댓글이 작성되었습니다."),

    //편집장
    EDITOR("승인 요청입니다.");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }

}