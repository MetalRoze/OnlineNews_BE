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

    @Builder
    public MainArticleDto(Long id, Category category, String articleTitle, String articleSubTitle) {
        this.id = id;
        this.category = category;
        this.articleTitle = articleTitle;
        this.articleSubTitle = articleSubTitle;
    }

    public static MainArticleDto fromEntity(MainArticle mainArticle){
        return new MainArticleDto(
                mainArticle.getId(),
                mainArticle.getCategory(),
                mainArticle.getArticle().getTitle(),
                mainArticle.getArticle().getSubtitle()
        );
    }
}
