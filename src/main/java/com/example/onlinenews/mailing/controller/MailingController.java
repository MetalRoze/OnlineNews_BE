package com.example.onlinenews.mailing.controller;

import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.mailing.api.MailingAPI;
import com.example.onlinenews.mailing.service.MailingService;
import jakarta.mail.MessagingException;
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

    @Override
    public ResponseEntity<?> sendDailyMailTest(String to) {
        try {
            mailingService.setHtmlMail(to);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionCode.EMAIL_CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> subscribeMailing(Long userId) {
        mailingService.subscribeMailing(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> unsubscribeMailing(Long userId) {
        mailingService.unsubscribeMailing(userId);
        return ResponseEntity.ok().build();
    }
}
