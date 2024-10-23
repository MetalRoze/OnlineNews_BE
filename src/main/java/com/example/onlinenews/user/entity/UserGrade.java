package com.example.onlinenews.user.entity;

public enum UserGrade {
    SYSTEM_ADMIN(9),
    EDITOR(9),
    REPORTER(7),
    INTERN_REPORTER(5),
    CITIZEN_REPORTER(4),
    GENERAL_MEMBER(3);

    private final int value;

    UserGrade(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
