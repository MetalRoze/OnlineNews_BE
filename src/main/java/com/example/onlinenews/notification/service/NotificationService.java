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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    //기자가 기사 작성 -> 편집장한테 승인요청 알림
    public void createRequestNoti (User user, Article article){
        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.EDITOR_REQUEST)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public void createApprovedNoti (User user, Article article){
        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_ACCEPTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createHeldNoti (User user, Article article){
        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_HELD)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createRejectedNoti (User user, Article article){
        Notification notification = Notification.builder()
                .user(user)
                .article(article)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_REJECTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
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

    // 사용자의 알림 중에서 status 별로 조회
    public List<NotificationDto> getByType(String email, String keyword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        Optional<Notification> optionalNotification = notificationRepository.findByUser(user);
        if(optionalNotification.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }

        NotificationType enumStatus = switch (keyword.toLowerCase()) {
            case "request" -> NotificationType.EDITOR_REQUEST;
            case "enroll" -> NotificationType.EDITOR_ENROLL_REPORTER;
            default -> throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        };

        return notificationRepository.findNotificationsByUserAndType(user, enumStatus).stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());

    }
}
