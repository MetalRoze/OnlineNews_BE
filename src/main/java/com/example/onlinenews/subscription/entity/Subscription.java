package com.example.onlinenews.subscription.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Publisher publisher;

    @Column(nullable = false)
    private LocalDateTime createdAt; //구독 날짜

    @Builder
    public Subscription(LocalDateTime created_at) {
        this.createdAt = created_at;
    }
}
