package com.example.onlinenews.notification.service;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.like.entity.ArticleLike;
import com.example.onlinenews.notification.dto.EditorNotificationDto;
import com.example.onlinenews.notification.dto.JournalistNotificationDto;
import com.example.onlinenews.notification.entity.EditorNotification;
import com.example.onlinenews.notification.entity.JournalistNotification;
import com.example.onlinenews.notification.entity.Notification;
import com.example.onlinenews.notification.entity.NotificationType;
import com.example.onlinenews.notification.repository.NotificationRepository;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
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

    public void createRequestNoti (Request request){
        User editorUser = userRepository.findByPublisherAndGrade(request.getUser().getPublisher(), UserGrade.EDITOR).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        EditorNotification notification = EditorNotification.builder()
                .user(editorUser)
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.EDITOR_REQUEST)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createEnrollNoti (Request request){
        User editorUser = userRepository.findByPublisherAndGrade(request.getUser().getPublisher(), UserGrade.EDITOR).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        EditorNotification notification = EditorNotification.builder()
                .user(editorUser)
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.EDITOR_ENROLL_REPORTER)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createApprovedNoti (Request request){
        JournalistNotification notification = JournalistNotification.builder()
                .user(request.getUser())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_ACCEPTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createHeldNoti (Request request){
        JournalistNotification notification = JournalistNotification.builder()
                .user(request.getUser())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_HELD)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createRejectedNoti (Request request){
        JournalistNotification notification = JournalistNotification.builder()
                .user(request.getUser())
                .request(request)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_APPROVAL_REJECTED)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }
    public void createLikeNoti (ArticleLike articleLike){
        JournalistNotification notification = JournalistNotification.builder()
                .user(articleLike.getArticle().getUser())
                .articleLike(articleLike)
                .createdAt(LocalDateTime.now())
                .type(NotificationType.REPORTER_LIKE)
                .isRead(false)
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

    public List<EditorNotificationDto> editorNotiList(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        if(user.getGrade().getValue()<9){
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }

        return notificationRepository.findByUser(user).stream()
                .filter(notification -> notification instanceof EditorNotification)
                .map(notification -> EditorNotificationDto.fromEntity((EditorNotification) notification))
                .collect(Collectors.toList());
    }
    public List<JournalistNotificationDto> journalistNotiList(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        return notificationRepository.findByUser(user).stream()
                .filter(notification -> notification instanceof JournalistNotification)
                .map(notification -> JournalistNotificationDto.fromEntity((JournalistNotification) notification))
                .collect(Collectors.toList());
    }
    public List<EditorNotificationDto> editorNotiListByType(String email, NotificationType notificationType) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List <Notification> notifications = notificationRepository.findByUser(user);
        if(notifications.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }
        return notificationRepository.findNotificationsByUserAndType(user, notificationType).stream()
                .filter(notification -> notification instanceof EditorNotification)
                .map(notification -> EditorNotificationDto.fromEntity((EditorNotification) notification))
                .collect(Collectors.toList());

    }

    public List<JournalistNotificationDto> journalNotiListByType(String email, NotificationType notificationType) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        List <Notification> notifications = notificationRepository.findByUser(user);
        if(notifications.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_MISMATCH);
        }
        return notificationRepository.findNotificationsByUserAndType(user,notificationType).stream()
                .filter(notification -> notification instanceof JournalistNotification)
                .map(notification -> JournalistNotificationDto.fromEntity((JournalistNotification) notification))
                .collect(Collectors.toList());

    }
}
