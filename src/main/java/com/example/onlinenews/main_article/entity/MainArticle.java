package com.example.onlinenews.main_article.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class MainArticle {
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
    private Article article;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //1번이 헤드라인 기사임
    @Column
    private int displayOrder;

    @Builder
    public MainArticle(User user, Article article, LocalDateTime createdAt, int displayOrder) {
        this.user = user;
        this.article = article;
        this.createdAt = createdAt;
        this.displayOrder = displayOrder;
    }
}
