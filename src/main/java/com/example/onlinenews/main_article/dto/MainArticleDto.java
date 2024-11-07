package com.example.onlinenews.main_article.dto;

import com.example.onlinenews.main_article.entity.MainArticle;
import lombok.Builder;
import lombok.Data;

@Data
public class MainArticleDto {
    private Long id;
    private int displayOrder;
    private String articleTitle;
    private String articleSubTitle;
//    private String img_url;

    @Builder
    public MainArticleDto(Long id, int displayOrder, String articleTitle, String articleSubTitle) {
        this.id = id;
        this.displayOrder = displayOrder;
        this.articleTitle = articleTitle;
        this.articleSubTitle = articleSubTitle;
    }

    public static MainArticleDto fromEntity(MainArticle mainArticle){
        return new MainArticleDto(
                mainArticle.getId(),
                mainArticle.getDisplayOrder(),
                mainArticle.getArticle().getTitle(),
                mainArticle.getArticle().getSubtitle()
        );
    }
}
