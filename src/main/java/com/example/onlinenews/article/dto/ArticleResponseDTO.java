package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ArticleResponseDTO {
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime holdAt;
    private LocalDateTime approvedAt;
    private RequestStatus state;
    private Boolean isPublic;
    private int views;
    private List<String> images;
}
