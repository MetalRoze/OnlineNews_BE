package com.example.onlinenews.main_article.dto;

import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.article_img.entity.ArticleImg;
import com.example.onlinenews.main_article.entity.MainArticle;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MainArticleDto {
    private Long id;
    private Category category;
    private String articleTitle;
    private String articleSubTitle;
    private int views;
    private String publisherName;
    private ArticleImg articleImg;

    @Builder
    public MainArticleDto(Long id, Category category, String articleTitle, String articleSubTitle, int views, String publisherName,  ArticleImg articleImg) {
        this.id = id;
        this.category = category;
        this.articleTitle = articleTitle;
        this.articleSubTitle = articleSubTitle;
        this.views = views;
        this.publisherName = publisherName;
        this.articleImg = articleImg;
    }

    public static MainArticleDto fromEntity(MainArticle mainArticle){
        ArticleImg headlineImage = null;
        List<ArticleImg> headlineImages = mainArticle.getArticle().getImages();
        if(!headlineImages.isEmpty()){
            headlineImage = mainArticle.getArticle().getImages().get(0);
        }
        return new MainArticleDto(
                mainArticle.getId(),
                mainArticle.getCategory(),
                mainArticle.getArticle().getTitle(),
                mainArticle.getArticle().getSubtitle(),
                mainArticle.getArticle().getViews(),
                mainArticle.getPublisher().getName(),
                headlineImage
        );
    }
}
