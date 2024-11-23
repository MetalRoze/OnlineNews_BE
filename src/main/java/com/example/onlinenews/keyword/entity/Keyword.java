package com.example.onlinenews.keyword.entity;

import com.example.onlinenews.keyword_article_like.entity.KeywordArticleLike;
import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String keyword;

    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeywordArticleLike> keywordArticleLikes = new ArrayList<>();

    @Builder
    public Keyword(User user, String keyword) {
        this.user = user;
        this.keyword = keyword;
    }
}
