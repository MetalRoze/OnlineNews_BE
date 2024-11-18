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
    private String userEmail;
    private int userGrade;
    private String requestTitle;
    private Long articleId;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private RequestStatus status;
    private String type;

    public RequestDto(Long id, Long userId, String userName, String userEmail, int userGrade, String requestTitle, Long articleId, LocalDateTime createdAt,LocalDateTime confirmedAt, RequestStatus status, String type) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userGrade = userGrade;
        this.requestTitle = requestTitle;
        this.articleId = articleId;
        this.createdAt = createdAt;
        this.confirmedAt=confirmedAt;
        this.status = status;
        this.type = type;
    }

    public static RequestDto fromEntity(Request request) {
        Article article = request.getArticle();
        return new RequestDto(
                request.getId(),
                request.getUser().getId(),
                request.getUser().getName(),
                request.getUser().getEmail(),
                request.getUser().getGrade().getValue(),
                request.getTitle(),
                article!=null? article.getId() : null,
                request.getCreatedAt(),
                request.getConfirmedAt(),
                request.getStatus(),
                request.getType()
        );
    }
}
