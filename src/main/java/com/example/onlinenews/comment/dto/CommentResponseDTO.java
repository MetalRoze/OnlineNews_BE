package com.example.onlinenews.comment.dto;

import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.like.repository.CommentLikeRepository;
import com.example.onlinenews.user.entity.User;
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
    private boolean likeStatus;

    public CommentResponseDTO(Comment comment, User user, CommentLikeRepository commentLikeRepository) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikeCount();
        this.replies = comment.getReplies() != null
                ? comment.getReplies().stream()
                .map(reply -> new CommentResponseDTO(reply, user, commentLikeRepository)) // 자식 댓글도 좋아요 상태 포함
                .toList()
                : List.of();
        this.userId=comment.getUser().getId();
        this.userEmail=comment.getUser().getEmail();
        this.userName=comment.getUser().getName();
        this.userImg=comment.getUser().getImg();
        this.likeStatus = commentLikeRepository.existsByUserAndComment(user, comment);
    }
}
