package com.example.onlinenews.comment.dto;

import com.example.onlinenews.comment.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<CommentResponseDTO> replies;
    private long userId;
    private String userEmail;
    private String userName;
    private String userImg;
    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikeCount();
        this.replies = comment.getReplies() != null
                ? comment.getReplies().stream()
                .map(CommentResponseDTO::new)
                .toList()
                : List.of();
        this.userId=comment.getUser().getId();
        this.userEmail=comment.getUser().getEmail();
        this.userName=comment.getUser().getName();
        this.userImg=comment.getUser().getImg();
    };
}
