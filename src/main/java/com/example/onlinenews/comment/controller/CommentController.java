package com.example.onlinenews.comment.controller;

import com.example.onlinenews.comment.api.CommentAPI;
import com.example.onlinenews.comment.dto.CommentReplRequestDTO;
import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import com.example.onlinenews.comment.service.CommentService;
import com.example.onlinenews.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController  implements CommentAPI {

    private final CommentService commentService;
    private final AuthService authService;

    @Override
    public ResponseEntity<CommentResponseDTO> createComment(HttpServletRequest httpServletRequest,
                                                            CommentRequestDTO requestDTO) {
        String email = authService.getEmailFromToken(httpServletRequest);

        CommentResponseDTO responseDTO = commentService.createComment(email, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<CommentResponseDTO> createReply(HttpServletRequest httpServletRequest,
                                                          @RequestBody CommentReplRequestDTO requestDTO) {
        String email = authService.getEmailFromToken(httpServletRequest);
        CommentResponseDTO responseDTO = commentService.createReply(email, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByArticle(Long articleId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByArticle(articleId);
        return ResponseEntity.ok(comments);
    }

    @Override
    public ResponseEntity<CommentResponseDTO> updateComment(CommentReplRequestDTO requestDTO) {
        CommentResponseDTO updatedComment = commentService.updateComment(requestDTO);
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
