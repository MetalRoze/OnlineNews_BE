package com.example.onlinenews.notification.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.dto.NotificationDto;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.repository.NotificationRepository;
import com.example.onlinenews.request.dto.RequestDto;
import com.example.onlinenews.request.entity.RequestStatus;
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
    private final ArticleRepository articleRepository;
    private final NotificationRepository notificationRepository;

    //기자가 기사 작성 -> 편집장한테 승인요청 알림
    public void createRequestNoti (User user, Article article){
        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.EDITOR)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
    public List<NotificationDto> list(){
        return notificationRepository.findAll().stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }
    public NotificationDto read(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ExceptionCode.NOTIFICATION_NOT_FOUND));
        return NotificationDto.fromEntity(notification);
    }
    //알림 읽음
    @Transactional
    public boolean updateIsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ExceptionCode.NOTIFICATION_NOT_FOUND));
        notification.updateIsRead(true);
        return notification.isRead();
    }
    //스위치케이스 덜적은거입니다...
    public List<NotificationDto> getByType(String keyword) {
        NotificationType enumStatus = switch (keyword.toLowerCase()) {
            case "request" -> NotificationType.EDITOR;
            case "comment" -> NotificationType.REPORTER_COMMENT;
            case "reply" -> NotificationType.USER_REPLY;
            default -> throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        };

        return notificationRepository.findNotificationsByType(enumStatus).stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }
}
