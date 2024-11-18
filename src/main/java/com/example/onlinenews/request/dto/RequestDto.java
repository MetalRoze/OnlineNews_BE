package com.example.onlinenews.request.dto;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long articleId;
    private String articleTitle;
    private LocalDateTime createdAt;
    private RequestStatus status;
    private String type;


    public RequestDto(Long id, Long userId, String userName, Long articleId, String articleTitle, LocalDateTime createdAt, RequestStatus status, String type) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.createdAt = createdAt;
        this.status = status;
        this.type=type;
    }

    public static RequestDto fromEntity(Request request) {
        Article article = request.getArticle();
        return new RequestDto(
                request.getId(),
                request.getUser().getId(),
                request.getUser().getName(),
                article != null ? article.getId() : null,
                article != null ? article.getTitle() : null,
                request.getCreatedAt(),
                request.getStatus(),
                request.getType()
        );
    }
}
