package com.example.onlinenews.article.dto;

import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.article_img.entity.ArticleImg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleUpdateRequestDTO {
    private String title;
    private String subtitle;
    private String content;
    private Category category;
    private List<ArticleImg> images;
    private Boolean isPublic;
}
