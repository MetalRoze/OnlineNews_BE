package com.example.onlinenews.notification.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.dto.RequestNotificationDto;
import com.example.onlinenews.notification.dto.RequestStatusNotificationDto;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.repository.NotificationRepository;
import com.example.onlinenews.request.entity.Request;
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

    public void createApprovedNoti (Request request){
        Notification notification = Notification.builder()
                .user(request.getUser())
                .article(request.getArticle())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_ACCEPTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createHeldNoti (Request request){
        Notification notification = Notification.builder()
                .user(request.getUser())
                .article(request.getArticle())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_HELD)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createRejectedNoti (Request request){
        Notification notification = Notification.builder()
                .user(request.getUser())
                .article(request.getArticle())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_REJECTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public RequestNotificationDto read(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ExceptionCode.NOTIFICATION_NOT_FOUND));
        return RequestNotificationDto.fromEntity(notification);
    }
    //알림 읽음
    @Transactional
    public boolean updateIsRead(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ExceptionCode.NOTIFICATION_NOT_FOUND));
        notification.updateIsRead(true);
        return notification.isRead();
    }

    // 사용자의 알림 중에서 status 별로 조회
    public List<RequestNotificationDto> getByType(String email, String keyword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List <Notification> notifications = notificationRepository.findByUser(user);
        if(notifications.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }
        NotificationType enumStatus = switch (keyword.toLowerCase()) {
            case "request" -> NotificationType.EDITOR_REQUEST;
            case "enroll" -> NotificationType.EDITOR_ENROLL_REPORTER;
            default -> throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        };

        return notificationRepository.findNotificationsByUserAndType(user, enumStatus).stream()
                .map(RequestNotificationDto::fromEntity)
                .collect(Collectors.toList());

    }

    public List<RequestStatusNotificationDto> getRequestStatusNotiesByType(String email, String keyword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List <Notification> notifications = notificationRepository.findByUser(user);
        if(notifications.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }
        NotificationType enumStatus = switch (keyword.toLowerCase()) {
            case "accepted" -> NotificationType.REPORTER_APPROVAL_ACCEPTED;
            case "hold" -> NotificationType.REPORTER_APPROVAL_HELD;
            case "rejected" ->NotificationType.REPORTER_APPROVAL_REJECTED;
            default -> throw new BusinessException(ExceptionCode.NOT_VALID_ERROR);
        };

        return notificationRepository.findNotificationsByUserAndType(user,enumStatus).stream()
                .map(RequestStatusNotificationDto::fromEntity)
                .collect(Collectors.toList());

    }
}
