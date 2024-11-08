package com.example.onlinenews.main_article.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.Data;

@Data
public class SelectArticleDto {
    private Long articleId;
    private int displayOrder;
    private Category category;
}
