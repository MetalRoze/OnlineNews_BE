package com.example.onlinenews.request.entity;


import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 요청 아이디 (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citizen_user_id")
    @JsonIgnore
    private User citizenUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column
    private String comment;

    @Builder
    public Request(User user, User citizenUser, Article article, LocalDateTime createdAt, RequestStatus status, String comment) {
        this.user = user;
        this.citizenUser=citizenUser;
        this.article = article;
        this.createdAt = createdAt;
        this.status = status;
        this.comment = comment;
    }

    //상태 업데이트
    public void updateStatus(RequestStatus newStatus, String comment){
        this.status = newStatus;
        this.comment = comment;
    }
}
