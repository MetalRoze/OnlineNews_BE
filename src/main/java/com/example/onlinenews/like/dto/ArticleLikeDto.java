package com.example.onlinenews.like.dto;

import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleLikeDto {
    private Long id;
    private String email;
    private Long articleId;
    private String articleTitle;
    private String publisherName;
    private LocalDateTime createdAt;

    @Builder
    public ArticleLikeDto(Long id, String email, Long articleId, String articleTitle, String publisherName, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.publisherName = publisherName;
        this.createdAt = createdAt;
    }

    public static ArticleLikeDto fromEntity(ArticleLike articleLike) {
        return new ArticleLikeDto(
                articleLike.getId(),
                articleLike.getUser().getEmail(),
                articleLike.getArticle().getId(),
                articleLike.getArticle().getTitle(),
                articleLike.getArticle().getUser().getPublisher().getName(),
                articleLike.getCreatedAt()
        );
    }
}

