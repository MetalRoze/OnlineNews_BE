package com.example.onlinenews.notification.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.request.entity.Request;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("AdminNotification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorNotification extends Notification {
    //승인 요청, 시민기자 회원가입 시 입사 등록 알림
    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    public EditorNotification(Request request){
        this.request= request;
    }
}