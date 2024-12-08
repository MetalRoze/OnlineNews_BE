package com.example.onlinenews.mailing.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.Builder;
import lombok.Data;

@Data
public class MailArticleDto {
    private Long id;
    private String reporter;
    private Category category;
    private String articleTitle;
    private String articleSubTitle;
    private String articleContent;
    private int views;
    private String publisherName;
    private String articleImageUrl;

    @Builder
    public MailArticleDto(Long id, Category category, String articleTitle, String articleSubTitle, int views,
                          String reporter, String articleContent, String publisherName, String articleImageUrl) {
        this.id = id;
        this.category = category;
        this.articleTitle = articleTitle;
        this.articleSubTitle = articleSubTitle;
        this.views = views;
        this.publisherName = publisherName;
        this.articleImageUrl = articleImageUrl;
        this.reporter = reporter;
        this.articleContent = articleContent;
    }

}
