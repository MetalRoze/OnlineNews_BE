package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequestDTO {
    private Category category;
    private String title;
    private String subtitle;
    private String content;
}
