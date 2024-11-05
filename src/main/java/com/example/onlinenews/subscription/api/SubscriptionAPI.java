package com.example.onlinenews.subscription.api;

import com.example.onlinenews.subscription.dto.SubscriptionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.onlinenews.subscription.dto.SubscriptionCreateRequestDto;

import java.util.List;

@RequestMapping("/api/subscription")
@Tag(name = "Subscription", description = "구독 관련 API")
public interface SubscriptionAPI {
    @GetMapping("")
    @Operation(summary = "사용자 구독 내역 조회", description = "해당 id의 사용자가 구독한 신문사 조회")
    List<SubscriptionDto> getSubscriptionsByUserId(HttpServletRequest servletRequest);

    @GetMapping("/check/{pub_id}")
    @Operation(summary = "신문사 구독 여부 확인", description = "해당 id의 사용자가 해당 신문사의 구독 여부를 확인합니다.")
    boolean isSubscribed(HttpServletRequest servletRequest, @PathVariable Long pub_id);

    @PostMapping("/subscribe") // 구독 요청 URL
    @Operation(summary = "신문사 구독 요청", description = "사용자가 신문사를 구독하도록 요청합니다.")
    ResponseEntity<?> subscribeCreate(HttpServletRequest servletRequest, @RequestBody SubscriptionCreateRequestDto requestDto);

    @DeleteMapping("/unsubscribe/{id}") //구독 취소
    @Operation(summary = "신문사 구독 취소", description = "사용자가 신문사의 구독 id로 취소를 요청합니다.")
    ResponseEntity<?> unsubscribe(HttpServletRequest servletRequest, @PathVariable Long id);

}
