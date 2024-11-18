package com.example.onlinenews.comment.dto;

import lombok.Data;

@Data
public class CommentReplRequestDTO {
    private Long commentID;
    private String content;
}
