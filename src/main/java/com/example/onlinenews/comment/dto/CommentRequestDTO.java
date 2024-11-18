package com.example.onlinenews.comment.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long userId;
    private Long articleId;
    private String content;
}
