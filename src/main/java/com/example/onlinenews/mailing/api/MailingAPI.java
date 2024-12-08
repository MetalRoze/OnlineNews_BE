package com.example.onlinenews.mailing.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/mailing")
@Tag(name = "Mailing", description = "메일링 관련 API")
public interface MailingAPI {

    @GetMapping("/sendTest")
    ResponseEntity<?> sendTestMailing(@RequestParam String to, @RequestParam String subject,
                                      @RequestParam String body);

    @GetMapping("/sendDailyTest")
    ResponseEntity<?> sendDailyMailTest(@RequestParam String to);

    @PostMapping("/subscribe")
    @Operation(summary = "메일링 수신 설정")
    ResponseEntity<?> subscribeMailing(@RequestParam Long userId);

    @DeleteMapping("/unsubscribe")
    @Operation(summary = "메일링 수신 해지")
    ResponseEntity<?> unsubscribeMailing(@RequestParam Long userId);

}
