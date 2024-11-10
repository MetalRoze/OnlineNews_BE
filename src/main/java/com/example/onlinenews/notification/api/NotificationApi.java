package com.example.onlinenews.notification.api;

import com.example.onlinenews.notification.dto.EditorNotificationDto;
import com.example.onlinenews.notification.dto.JournalistNotificationDto;
import com.example.onlinenews.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/notification")
@Tag(name = "Notification", description = "요청 API")
public interface NotificationApi {
//
//    @GetMapping("/{notificationId}")
//    @Operation(summary = "요청 id로 알림 조회", description = "요청아이디(path)로 해당 알림을 조회합니다.")
//    RequestNotificationDto read(@PathVariable Long notificationId);
//
    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음", description = "id(path)에 해당하는 알림을 읽음 처리합니다")
    ResponseEntity<?> updateIsRead(HttpServletRequest request,  @PathVariable Long notificationId);

    @GetMapping("/editor")
    @Operation(summary = "편집장이 알림 조회", description = "편집장이 알림 전체를 조회합니다")
    List<EditorNotificationDto> editorNotiList(HttpServletRequest request);

    @GetMapping("/journalist")
    @Operation(summary = "기자가 알림 조회", description = "기자가 알림 전체를 조회합니다")
    List<JournalistNotificationDto> journalistNotiList(HttpServletRequest request);


    @GetMapping("/editor/type")
    @Operation(summary = "편집장이 요청 type별로 요청 조회", description = "편집장이 타입을 입력(param)하여 해당 상태의 알림들을 조회합니다.")
    List<EditorNotificationDto> editorNotiListByType(HttpServletRequest request, @RequestParam NotificationType type);

    @GetMapping("/journalist/type")
    @Operation(summary = "기자가 요청 type별로 요청 조회", description = "기자가 타입을 입력(param)하여 해당 상태의 알림들을 조회합니다.")
    List<JournalistNotificationDto> journalNotiListByType(HttpServletRequest request, @RequestParam NotificationType type);



}
