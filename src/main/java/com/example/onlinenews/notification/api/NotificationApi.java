package com.example.onlinenews.notification.api;

import com.example.onlinenews.notification.dto.NotificationDto;
import com.example.onlinenews.request.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/notification")
@Tag(name = "Notification", description = "요청 API")
public interface NotificationApi {
    @GetMapping("")
    @Operation(summary = "전체 알림 조회", description = "전체 알림 목록을 조회합니다.")
    List<NotificationDto> list();

    @GetMapping("/{notificationId}")
    @Operation(summary = "요청 id로 알림 조회", description = "요청아이디(path)로 해당 알림을 조회합니다.")
    NotificationDto read(@PathVariable Long notificationId);
}
