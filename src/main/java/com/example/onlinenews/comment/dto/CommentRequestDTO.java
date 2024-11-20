package com.example.onlinenews.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long articleId;
    private String content;
}
