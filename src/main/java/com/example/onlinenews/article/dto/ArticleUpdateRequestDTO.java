package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.*;

@Data
public class ArticleUpdateRequestDTO {
    private String title;
    private String subtitle;
    private String content;
    private Category category;
    private Boolean isPublic;
}
