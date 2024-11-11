package com.example.onlinenews.rss.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RssArticleDto {
    private Long pubId;
    private String publisherName;
    private String title;
    private String subtitle;
    private String url;
    private String author;
    private String createdAt;
    private String imgUrl;

    @Builder
    public RssArticleDto(Long pubId, String publisherName, String title, String subtitle, String url, String author, String createdAt, String imgUrl) {
        this.pubId=pubId;
        this.publisherName = publisherName;
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
        this.author = author;
        this.createdAt=createdAt;
        this.imgUrl=imgUrl;
    }
}
