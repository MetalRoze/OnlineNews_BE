package com.example.onlinenews.comment.api;

import com.example.onlinenews.comment.dto.CommentReplRequestDTO;
import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comment")
@Tag(name = "Comment", description = "댓글 API")
public interface CommentAPI {
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(HttpServletRequest httpServletRequest,
                                                            @RequestBody CommentRequestDTO requestDTO);
    @PostMapping("/replies")
    public ResponseEntity<CommentResponseDTO> createReply(HttpServletRequest httpServletRequest,
                                                          @RequestBody CommentReplRequestDTO requestDTO);
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByArticle(@PathVariable Long articleId) ;
    @PutMapping("/edit")
    public ResponseEntity<CommentResponseDTO> updateComment(@RequestBody CommentReplRequestDTO requestDTO);
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id);
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeComment(@PathVariable Long id);
    @PostMapping("/{id}/unlike")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long id);
    }
