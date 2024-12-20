package com.example.onlinenews.request.entity;


import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.publisher.entity.Publisher;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime confirmedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column
    private String title;

    @Column
    private String comment;

    @Column
    private String type;

    @Builder
    public Request(User user, Publisher publisher, Article article, LocalDateTime createdAt, LocalDateTime confirmedAt,  RequestStatus status, String title, String comment, String type) {
        this.user = user;
        this.publisher = publisher;
        this.article = article;
        this.createdAt = createdAt;
        this.confirmedAt=confirmedAt;
        this.status = status;
        this.title = title;
        this.comment = comment;
        this.type=type;
    }

    //상태 업데이트
    public void updateStatus(RequestStatus newStatus, String comment){
        this.status = newStatus;
        this.comment = comment;
    }
    public void confirm() {
        this.confirmedAt = LocalDateTime.now();
    }
}
