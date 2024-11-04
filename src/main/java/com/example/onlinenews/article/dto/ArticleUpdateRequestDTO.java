package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleUpdateRequestDTO {
    private String title;

    private String subtitle;

    private String content;

    private Category category;
}
