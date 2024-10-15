package com.example.onlinenews.article_img.entity;

import com.example.onlinenews.article.entity.Article;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Article article;

    @Column(nullable = false)
    private String imgUrl;
}
