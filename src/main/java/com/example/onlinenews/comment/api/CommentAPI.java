package com.example.onlinenews.comment.api;

import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comment")
@Tag(name = "Comment", description = "댓글 API")
public interface CommentAPI {
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO requestDTO);
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<CommentResponseDTO> createReply(@PathVariable Long commentId, @RequestBody CommentRequestDTO requestDTO);
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByArticle(@PathVariable Long articleId) ;
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody CommentRequestDTO requestDTO);
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id);
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long id);
    @PostMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id);
    }
