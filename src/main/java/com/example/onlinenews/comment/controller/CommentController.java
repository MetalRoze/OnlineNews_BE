package com.example.onlinenews.comment.controller;

import com.example.onlinenews.comment.api.CommentAPI;
import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import com.example.onlinenews.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController  implements CommentAPI {

    private final CommentService commentService;

    @Override
    public ResponseEntity<CommentResponseDTO> createComment(CommentRequestDTO requestDTO) {
        CommentResponseDTO responseDTO = commentService.createComment(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<CommentResponseDTO> createReply(Long commentId, CommentRequestDTO requestDTO) {
        CommentResponseDTO responseDTO = commentService.createReply(commentId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByArticle(Long articleId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByArticle(articleId);
        return ResponseEntity.ok(comments);
    }

    @Override
    public ResponseEntity<CommentResponseDTO> updateComment(Long id, CommentRequestDTO requestDTO) {
        CommentResponseDTO updatedComment = commentService.updateComment(id, requestDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> likeComment(Long id) {
        commentService.likeComment(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unlikeComment(Long id) {
        commentService.unlikeComment(id);
        return ResponseEntity.ok().build();
    }
}
