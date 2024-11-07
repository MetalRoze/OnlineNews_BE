package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.*;

import java.util.List;

@Data
public class ArticleRequestDTO {
    private Category category;
    private String title;
    private String subtitle;
    private String content;
}
