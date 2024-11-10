package com.example.onlinenews.rss.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RssArticleDto {
    private String title;
    private String subtitle;
    private String url;
    private String author;
    private String createdAt;

    @Builder
    public RssArticleDto(String title, String subtitle, String url, String author, String createdAt) {
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
        this.author = author;
        this.createdAt=createdAt;
    }
}
