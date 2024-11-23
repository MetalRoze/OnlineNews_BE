package com.example.onlinenews.like.entity;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.keyword.entity.Keyword;
import com.example.onlinenews.keyword_article_like.entity.KeywordArticleLike;
import com.example.onlinenews.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArticleLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    private Article article;

    @OneToMany(mappedBy = "articleLike", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KeywordArticleLike> keywordArticleLikes = new ArrayList<>();

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
