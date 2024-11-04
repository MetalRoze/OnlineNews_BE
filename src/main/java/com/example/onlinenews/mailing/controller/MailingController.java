package com.example.onlinenews.mailing.controller;

import com.example.onlinenews.mailing.api.MailingAPI;
import com.example.onlinenews.mailing.service.MailingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MailingController implements MailingAPI {
    private final MailingService mailingService;

    @Override
    public ResponseEntity<?> sendTestMailing(String to, String subject, String body) {
        mailingService.sendSimpleMail(to, subject, body);
        return ResponseEntity.ok().build();
    }
}
