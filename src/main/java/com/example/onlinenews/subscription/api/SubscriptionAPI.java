package com.example.onlinenews.subscription.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.onlinenews.subscription.dto.SubscriptionAddRequestDto;

import java.util.List;

@RequestMapping("/api/subscription")
@Tag(name = "Subscription", description = "구독 관련 API")
public interface SubscriptionAPI {
//    @PostMapping("/{id}")
//    @Operation(summary = "사용자 구독 내역 조회", description = "해당 id의 사용자가 구독한 신문사 조회")
//    List<String> getSubscriptionsByUserId(@PathVariable("id") Long userId);
//
//    @GetMapping("/check")
//    @Operation(summary = "신문사 구독 여부 확인", description = "해당 id의 사용자가 해당 신문사의 구독 여부를 확인합니다.")
//    boolean isSubscribed(@RequestParam("userId") Long userId, @RequestParam("newspaperId") Long newspaperId);
//
//    @PostMapping("/subscribe") // 구독 요청 URL
//    @Operation(summary = "신문사 구독 요청", description = "사용자가 신문사를 구독하도록 요청합니다.")
//    ResponseEntity<?> subscribe(@RequestBody SubscriptionAddRequestDto requestDto);
//
//    @DeleteMapping("/unsubscribe") //구독 취소 !!!!!!!!!!!! 이거 Params이거 바꿔야댐!!!!!!!!!!!!!!!!!!!
//    @Operation(summary = "신문사 구독 취소", description = "사용자가 신문사의 구독 취소를 요청합니다.")
//    ResponseEntity<?> unsubscribe(@RequestParam("userId") Long userId, @RequestParam("newspaperId") Long newspaperId);

}
