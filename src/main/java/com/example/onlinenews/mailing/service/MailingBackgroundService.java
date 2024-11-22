package com.example.onlinenews.mailing.service;


import com.example.onlinenews.mailing.dto.MailArticleDto;
import com.example.onlinenews.mailing.entity.Mailing;
import com.example.onlinenews.mailing.repository.MailingRepository;
import jakarta.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailingBackgroundService {

    @Autowired
    private MailingService mailingService;

    @Autowired
    private MailingRepository mailingRepository;

    @Transactional
    @Scheduled(cron = "0 0 22 * * * ?")
    public void sendDailyEmails() {
        // 메일링 구독자를 가져옵니다.
        List<Mailing> mailingList = mailingRepository.findAll();

        List<MailArticleDto> headArticles = mailingService.getHeadArticlesByCategory();
        if (!headArticles.isEmpty() && !mailingList.isEmpty()) {
            String emailContent = mailingService.generateEmailTemplate(headArticles);

            // 오늘의 날짜 생성
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            for (Mailing mailing : mailingList) {
                String recipientEmail = mailing.getUser().getEmail();

                try {
                    // HTML 이메일 발송
                    mailingService.sendHtmlMail(recipientEmail, "오늘의 뉴스 다이제스트 - " + formattedDate, emailContent);
                    System.out.println("메일 발송 완료: " + recipientEmail);
                } catch (MessagingException e) {
                    System.err.println("메일 발송 실패: " + recipientEmail);
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("오늘은 보낼 메일이 없네요");
        }


    }
}
