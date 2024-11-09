package com.example.onlinenews.main_article.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.publisher.entity.Publisher;
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
    @JoinColumn(name = "publisher_id")
    @JsonIgnore
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonIgnore
    private Article article;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Category category;

    @Builder
    public MainArticle(Publisher publisher, Article article, LocalDateTime createdAt, Category category) {
        this.publisher = publisher;
        this.article = article;
        this.createdAt = createdAt;
        this.category = category;
    }
}
