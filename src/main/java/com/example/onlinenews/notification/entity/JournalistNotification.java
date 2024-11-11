package com.example.onlinenews.notification.entity;


import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.user.entity.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@DiscriminatorValue("JournalistNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class JournalistNotification extends Notification {
    //기사좋아요 알림, 댓글 알림, 승인 수락,보류, 거절 알림
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleLike articleLike;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    public JournalistNotification(User user, NotificationType type, LocalDateTime createdAt, boolean isRead, ArticleLike articleLike, Request request){
        super(user, type, createdAt, isRead);
        this.articleLike=articleLike;
        this.request = request;
    }

}

