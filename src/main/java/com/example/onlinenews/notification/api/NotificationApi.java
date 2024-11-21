package com.example.onlinenews.notification.api;

import com.example.onlinenews.notification.dto.RequestNotificationDto;
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


    @GetMapping("/journalist/request")
    @Operation(summary = "승인 요청 (기사, 시민기자 등록) 현황 알림", description = "기자가 승인요청에 대한 알림을 조회합니다")
    List<RequestNotificationDto> getJournalRequestNoti(HttpServletRequest request);



}
