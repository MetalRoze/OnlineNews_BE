package com.example.onlinenews.main_article.dto;

import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.main_article.entity.MainArticle;
import lombok.Builder;
import lombok.Data;

@Data
public class MainArticleDto {
    private Long id;
    private Category category;
    private String articleTitle;
    private String articleSubTitle;
    private int views;
    private String publisherName;

    @Builder
    public MainArticleDto(Long id, Category category, String articleTitle, String articleSubTitle, int views, String publisherName) {
        this.id = id;
        this.category = category;
        this.articleTitle = articleTitle;
        this.articleSubTitle = articleSubTitle;
        this.views = views;
        this.publisherName = publisherName;
    }

    public static MainArticleDto fromEntity(MainArticle mainArticle){
        return new MainArticleDto(
                mainArticle.getId(),
                mainArticle.getCategory(),
                mainArticle.getArticle().getTitle(),
                mainArticle.getArticle().getSubtitle(),
                mainArticle.getArticle().getViews(),
                mainArticle.getPublisher().getName()
        );
    }
}
