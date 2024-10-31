package com.example.onlinenews.notification.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.dto.NotificationDto;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.repository.NotificationRepository;
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
    public void createRequestNoti (Long userId, Long articleId){
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

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
}
