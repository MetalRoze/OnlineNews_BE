package com.example.onlinenews.notification.controller;

import com.example.onlinenews.notification.api.NotificationApi;
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

//    @Override
//    public RequestNotificationDto read(Long notificationId) {
//        return notificationService.read(notificationId);
//    }

    @Override
    public ResponseEntity<?> updateIsRead(HttpServletRequest request, Long notificationId) {
        String email = authService.getEmailFromToken(request);
        return ResponseEntity.ok(notificationService.updateIsRead(email, notificationId));
    }
//
//    @Override
//    public List<RequestNotificationDto> getByType(HttpServletRequest request, String keyword) {
//        String email = authService.getEmailFromToken(request);
//        return notificationService.getByType(email, keyword);
//    }
//
//    @Override
//    public List<RequestStatusNotificationDto> getRequestStatusNotiesByType(HttpServletRequest request, String keyword) {
//        String email = authService.getEmailFromToken(request);
//        return notificationService.getRequestStatusNotiesByType(email, keyword);
//    }
}
