package com.example.onlinenews.notification.controller;

import com.example.onlinenews.notification.api.NotificationApi;
import com.example.onlinenews.notification.dto.CommentNotificationDto;
import com.example.onlinenews.notification.dto.LikeNotificationDto;
import com.example.onlinenews.notification.service.NotificationService;
import com.example.onlinenews.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationApi {
    private final NotificationService notificationService;
    private final AuthService authService;

    @Override
    public ResponseEntity<?> updateIsRead(HttpServletRequest request, Long notificationId) {
        String email = authService.getEmailFromToken(request);
        return ResponseEntity.ok(notificationService.updateIsRead(email, notificationId));
    }

    @Override
    public List<?> getNotiByType(HttpServletRequest request, String type) {
        String email = authService.getEmailFromToken(request);
        return notificationService.getNotiByType(email, type);
    }

}
