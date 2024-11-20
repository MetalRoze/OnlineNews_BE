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
        return commentService.createComment(email, requestDTO);
    }

    @Override
    public ResponseEntity<CommentResponseDTO> createReply(HttpServletRequest httpServletRequest,
                                                          @RequestBody CommentReplRequestDTO requestDTO) {
        String email = authService.getEmailFromToken(httpServletRequest);
        return commentService.createReply(email, requestDTO);
    }

    @Override
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByArticle(HttpServletRequest httpServletRequest,Long articleId, String sortType) {
        String email = authService.getEmailFromToken(httpServletRequest);
        List<CommentResponseDTO> comments = commentService.getCommentsByArticle(email, articleId, sortType);
        return ResponseEntity.ok(comments);
    }

    @Override
    public ResponseEntity<String> updateComment(CommentReplRequestDTO requestDTO) {
        return commentService.updateComment(requestDTO);
    }

    @Override
    public ResponseEntity<String> deleteComment(Long id) {
        return commentService.deleteComment(id);
    }

    @Override
    public ResponseEntity<String> likeComment(HttpServletRequest httpServletRequest, Long id) {
        String email = authService.getEmailFromToken(httpServletRequest);
        return commentService.likeComment(id, email);
    }

    @Override
    public ResponseEntity<String> unlikeComment(HttpServletRequest httpServletRequest, Long id) {
        String email = authService.getEmailFromToken(httpServletRequest);
        return commentService.unlikeComment(id, email);
    }
}
