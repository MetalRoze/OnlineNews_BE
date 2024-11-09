package com.example.onlinenews.notification.entity;


import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.request.entity.Request;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("JournalistNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalistNotification extends Notification {
    //기사좋아요 알림, 댓글 알림, 승인 수락,보류, 거절 알림
    @ManyToOne(fetch = FetchType.LAZY)
    private ArticleLike articleLike;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    @Builder
    public JournalistNotification(ArticleLike articleLike, Request request){
        this.articleLike = articleLike;
        this.request = request;
    }

}

