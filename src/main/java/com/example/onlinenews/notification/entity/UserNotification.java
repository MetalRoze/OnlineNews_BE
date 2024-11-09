package com.example.onlinenews.notification.entity;

import com.example.onlinenews.article.entity.Article;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("UserNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserNotification extends Notification {
    // 댓글, 대댓글, 댓글 좋아요
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;
}
