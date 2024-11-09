package com.example.onlinenews.publisher.entity;

import lombok.Getter;

@Getter
public enum Type {
    GENERAL("종합지"),
    ENTERTAINMENT("방송/통신"),
    ECONOMY("경제"),
    INTERNET("인터넷"),
    IT("IT"),
    MAGAZINE("매거진"),
    SPECIALIZED("전문지");

    private final String description;

    Type(String description) {this.description = description;}
    public String getDescription() {return description;}

}
