package com.example.onlinenews.notification.api;

import com.example.onlinenews.notification.dto.NotificationDto;
import com.example.onlinenews.request.dto.RequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{notificationId}")
    @Operation(summary = "알림 읽음", description = "id(path)에 해당하는 알림을 읽음 처리합니다")
    ResponseEntity<?> updateIsRead(@PathVariable Long notificationId);

    @GetMapping("/type")
    @Operation(summary = "요청 type별로 요청 조회", description = "타입을 입력(param)하여 해당 상태의 알림들을 조회합니다.")
    List<NotificationDto> getByType(@RequestParam String keyword);
}
