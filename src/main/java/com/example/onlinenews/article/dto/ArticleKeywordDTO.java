package com.example.onlinenews.article.dto;

import com.example.onlinenews.keyword.entity.Keyword;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleKeywordDTO {
    private Long articleId;
    private List<String> keyword;
}
