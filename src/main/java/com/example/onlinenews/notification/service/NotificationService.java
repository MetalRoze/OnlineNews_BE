package com.example.onlinenews.notification.service;

import com.example.onlinenews.comment.entity.Comment;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.like.entity.CommentLike;
import com.example.onlinenews.notification.dto.CommentNotificationDto;
import com.example.onlinenews.notification.dto.LikeNotificationDto;
import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.entity.UserNotification;
import com.example.onlinenews.notification.repository.NotificationRepository;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    //시민기자 등록 현황 알림
    public void createEnrollApprovedNoti (Request request){
        JournalistNotification notification = JournalistNotification.builder()
                .user(request.getUser())
                .type(NotificationType.ENROLL)
                .message("시민기자 등록 요청이" + " "+request.getStatus().getDescription()+"되었습니다.")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(request.getId())
                .senderName(request.getPublisher().getName())
                .comment(null)
                .build();

        notificationRepository.save(notification);
    }
    //기사 승인 현황 알림
    public void createRequestApprovedNoti (Request request){
        JournalistNotification notification = JournalistNotification.builder()
                .user(request.getUser())
                .type(NotificationType.REQUEST)
                .message(request.getArticle().getTitle()+" 기사가 "+request.getStatus().getDescription()+"되었습니다.")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(request.getId())
                .senderName(request.getPublisher().getName())
                .comment(request.getComment())
                .build();
        notificationRepository.save(notification);
    }

    //좋아요 알림
    public void createLikeNoti (ArticleLike articleLike){
        JournalistNotification notification = JournalistNotification.builder()
                .user(articleLike.getArticle().getUser())
                .type(NotificationType.REPORTER_LIKE)
                .message(articleLike.getArticle().getTitle()+" "+NotificationType.REPORTER_LIKE.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(articleLike.getId())
                .senderName(articleLike.getUser().getName())
                .comment(null)
                .build();
        notificationRepository.save(notification);
    }
    //댓글 알림
    public void createCommentNoti (Comment comment){
        JournalistNotification notification = JournalistNotification.builder()
                .user(comment.getArticle().getUser())
                .type(NotificationType.REPORTER_COMMENT)
                .message(comment.getArticle().getTitle()+" "+NotificationType.REPORTER_COMMENT.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(comment.getId())
                .senderName(comment.getUser().getName())
                .comment(comment.getContent())
                .build();
        notificationRepository.save(notification);
    }
    //댓글 좋아요 알림
    public void createCommentLikeNoti (CommentLike commentLike){
        UserNotification notification = UserNotification.builder()
                .user(commentLike.getComment().getUser())
                .type(NotificationType.USER_LIKE)
                .message(commentLike.getComment().getContent()+" "+NotificationType.USER_LIKE.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(commentLike.getId())
                .senderName(commentLike.getUser().getName())
                .reply(null)
                .build();
        notificationRepository.save(notification);
    }
    //댓글 좋아요 알림
    public void createReplyNoti (Comment reply){
        UserNotification notification = UserNotification.builder()
                .user(reply.getParent().getUser())
                .type(NotificationType.USER_REPLY)
                .message(reply.getParent().getContent()+" "+NotificationType.USER_REPLY.getMessage())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .targetId(reply.getId())
                .senderName(reply.getUser().getName())
                .reply(reply.getContent())
                .build();
        notificationRepository.save(notification);
    }
    //알림 읽음
    @Transactional
    public boolean updateIsRead(String email, Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ExceptionCode.NOTIFICATION_NOT_FOUND));
        if(!notification.getUser().getEmail().equals(email)){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }
        notification.updateIsRead(true);
        return notification.isRead();
    }

    private List<CommentNotificationDto> getJournalRequestNoti(User user) {
        List<Notification> notifications = notificationRepository.findByUserAndTypes(user, List.of(NotificationType.REQUEST, NotificationType.ENROLL));

        return notifications.stream()
                .filter(notification -> notification instanceof JournalistNotification)
                .map(notification -> CommentNotificationDto.fromEntity((JournalistNotification) notification))
                .collect(Collectors.toList());
    }

    private List<LikeNotificationDto> getJournalLikeNoti(User user) {
        return notificationRepository.findByUserAndType(user, NotificationType.REPORTER_LIKE).stream()
                .filter(notification -> notification instanceof JournalistNotification)
                .map(notification -> LikeNotificationDto.fromEntity((JournalistNotification) notification))
                .collect(Collectors.toList());
    }

    private List<CommentNotificationDto> getJournalCommentNoti(User user) {
        return notificationRepository.findByUserAndType(user, NotificationType.REPORTER_COMMENT).stream()
                .filter(notification -> notification instanceof JournalistNotification)
                .map(notification -> CommentNotificationDto.fromEntity((JournalistNotification) notification))
                .collect(Collectors.toList());
    }
    private List<LikeNotificationDto> getUserCommentLikeNoti(User user) {
        return notificationRepository.findByUserAndType(user, NotificationType.USER_LIKE).stream()
                .filter(notification -> notification instanceof UserNotification)
                .map(notification -> LikeNotificationDto.fromEntity((UserNotification) notification))
                .collect(Collectors.toList());
    }

    private List<CommentNotificationDto> getUserReplyNoti(User user) {
        return notificationRepository.findByUserAndType(user, NotificationType.USER_REPLY).stream()
                .filter(notification -> notification instanceof UserNotification)
                .map(notification -> CommentNotificationDto.fromEntity((UserNotification) notification))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<?> getNotiByType(String email, String type) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        switch (type){
            case "comment":
                if(user.getGrade().getValue()>3){
                    return getJournalCommentNoti(user);
                }
                else{
                    return getUserReplyNoti(user);
                }
            case "request":
                if(user.getGrade().getValue()>3){
                    return getJournalRequestNoti(user);
                }
                else{
                    throw new BusinessException(ExceptionCode.USER_MISMATCH);
                }
            case "like":
                if(user.getGrade().getValue()>3){
                    return getJournalLikeNoti(user);
                }
                else{
                    return getUserCommentLikeNoti(user);
                }
            default:
                throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        }
    }

}
