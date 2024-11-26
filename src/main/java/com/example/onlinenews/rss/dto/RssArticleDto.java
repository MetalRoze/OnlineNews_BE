package com.example.onlinenews.rss.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RssArticleDto {
    private Long publisherId;
    private String publisherName;
    private String title;
    private String subtitle;
    private String url;
    private String userName;

    @Builder
    public RssArticleDto(Long publisherId, String publisherName, String title, String subtitle, String url, String userName) {
        this.publisherId=publisherId;
        this.publisherName = publisherName;
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
        this.userName = userName;
    }
}
