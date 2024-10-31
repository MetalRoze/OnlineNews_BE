package com.example.onlinenews.notification.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.dto.NotificationRequestCreateDto;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.repository.NotificationRepository;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final NotificationRepository notificationRepository;

    //기자가 기사 작성 -> 편집장한테 승인요청 알림
    public void createRequestNoti (NotificationRequestCreateDto notificationRequestCreateDto){
        User user = userRepository.findById(notificationRequestCreateDto.getUserId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Article article = articleRepository.findById(notificationRequestCreateDto.getArticleId()).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.EDITOR)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }
}
