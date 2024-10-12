package com.example.onlinenews.article.entity;

import lombok.Getter;

@Getter
public enum State{
    REQUESTED("승인 요청된 상태"),
    HOLDING("승인 보류된 상태"),
    APPROVED("기사 승인된 상태");

    // 한글 설명을 리턴하는 메소드
    private final String description;

    // 생성자
    State(String description) {
        this.description = description;
    }

}
