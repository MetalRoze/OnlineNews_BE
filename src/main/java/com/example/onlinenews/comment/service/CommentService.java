package com.example.onlinenews.comment.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.comment.dto.CommentReplRequestDTO;
import com.example.onlinenews.comment.dto.CommentRequestDTO;
import com.example.onlinenews.comment.dto.CommentResponseDTO;
import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.comment.repository.CommentRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.like.entity.CommentLike;
import com.example.onlinenews.like.repository.CommentLikeRepository;
import com.example.onlinenews.notification.service.NotificationService;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    // 댓글 작성
    public ResponseEntity<CommentResponseDTO> createComment(String email, CommentRequestDTO requestDTO) {
        User user = userRepository.findByEmail(email)
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

        CommentResponseDTO responseDTO = new CommentResponseDTO(savedComment, user, commentLikeRepository);
        //댓글 알림
        notificationService.createCommentNoti(savedComment);
        return ResponseEntity.ok(responseDTO);
    }

    // 대댓글 작성
    public ResponseEntity<CommentResponseDTO> createReply(String email, CommentReplRequestDTO requestDTO) {
        Comment parentComment = commentRepository.findById(requestDTO.getCommentID())
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Comment reply = Comment.builder()
                .user(user)
                .parent(parentComment)
                .article(parentComment.getArticle())
                .content(requestDTO.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(reply);
        parentComment.getReplies().add(reply);
        commentRepository.save(parentComment);

        CommentResponseDTO responseDTO = new CommentResponseDTO(reply, user, commentLikeRepository);

        return ResponseEntity.ok(responseDTO);  // 새로 작성된 대댓글만 반환
    }


    // 특정 기사의 댓글, 대댓글 조회
    public List<CommentResponseDTO> getCommentsByArticle(String email, Long articleId, String sortType){
        Sort sort;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        switch (sortType.toLowerCase()) {
            case "like":
                sort = Sort.by(Sort.Order.desc("likeCount"));
                break;
            case "latest":
                sort = Sort.by(Sort.Order.desc("createdAt"));
                break;
            case "oldest":
                sort = Sort.by(Sort.Order.asc("createdAt"));
                break;
            default:
                sort = Sort.by(Sort.Order.desc("createdAt"));
        }

        List<Comment> comments = commentRepository.findByArticleIdAndParentIsNull(articleId, sort);
//        return comments.stream().map(CommentResponseDTO::new).collect(Collectors.toList());

        return comments.stream()
                .map(comment -> new CommentResponseDTO(comment, user, commentLikeRepository))
                .collect(Collectors.toList());

    }

    // 댓글 수정
    public ResponseEntity<String> updateComment(CommentReplRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(requestDTO.getCommentID())
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.setContent(requestDTO.getContent());
        commentRepository.save(comment);

        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    public ResponseEntity<String> deleteComment(Long id) {
        commentRepository.deleteById(id);

        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

    // 좋아요
    public ResponseEntity<String> likeComment(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        if (commentLikeRepository.existsByUserAndComment(user, comment)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 좋아요를 눌렀습니다.");
        }

        CommentLike commentLike = new CommentLike();
        commentLike.setUser(user);
        commentLike.setComment(comment);
        commentLikeRepository.save(commentLike);

        // 댓글의 좋아요 카운트 증가
        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        return ResponseEntity.ok("좋아요");
    }

    // 좋아요 취소
    public ResponseEntity<String> unlikeComment(Long id, String email) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        CommentLike like = commentLikeRepository.findByUserAndComment(user, comment);


        commentLikeRepository.delete(like);

        comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        commentRepository.save(comment);

        return ResponseEntity.ok("좋아요 취소");
    }
}
