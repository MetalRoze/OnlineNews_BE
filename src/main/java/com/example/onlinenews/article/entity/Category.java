package com.example.onlinenews.article.entity;

import lombok.Getter;

@Getter
public enum Category {
    SOCIAL("사회"),
    ECONOMY("경제"),
    LIFE_CULTURE("생활/문화"),
    ENTERTAINMENT("연예"),
    SCIENCE_TECH("기계/IT"),
    POLITICS("정치"),
    OPINION("오피니언");

    private final String description;

    // 생성자
    Category(String description) {
        this.description = description;
    }

    // 한글 설명을 리턴하는 메소드
    public String getDescription() {
        return description;
    }
}
