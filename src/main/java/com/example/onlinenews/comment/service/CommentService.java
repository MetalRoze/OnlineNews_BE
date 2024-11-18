package com.example.onlinenews.comment.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.comment.repository.CommentRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    public CommentResponseDTO createComment(CommentRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(requestDTO.getArticleId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        Comment comment = Comment.builder()
                .user(user)
                .article(article)
                .content(requestDTO.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDTO(savedComment);
    }

    // 대댓글 작성
    public CommentResponseDTO createReply(Long commentId, CommentRequestDTO requestDTO) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Comment reply = Comment.builder()
                .user(user)
                .parent(parentComment)
                .article(parentComment.getArticle())
                .content(requestDTO.getContent())
                .build();

        Comment savedReply = commentRepository.save(reply);
        return new CommentResponseDTO(savedReply);
    }

    // 특정 기사의 댓글, 대댓글 조회
    public List<CommentResponseDTO> getCommentsByArticle(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleIdAndParentIsNull(articleId);
        return comments.stream().map(CommentResponseDTO::new).collect(Collectors.toList());
    }

    // 댓글 수정
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.setContent(requestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDTO(updatedComment);
    }

    // 댓글 삭제
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // 좋아요
    public void likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);
    }

    // 좋아요 취소
    public void unlikeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);
    }
}
