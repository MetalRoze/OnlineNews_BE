package com.example.onlinenews.notification.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("EditorNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class EditorNotification extends Notification {
    //승인 요청, 시민기자 회원가입 시 입사 등록 알림
    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    public EditorNotification(User user, NotificationType type, Request request, LocalDateTime createdAt, boolean isRead) {
        super(user, type, createdAt, isRead);
        this.request = request;
    }
}