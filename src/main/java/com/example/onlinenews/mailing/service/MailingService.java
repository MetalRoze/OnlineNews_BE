package com.example.onlinenews.mailing.service;

import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.mailing.dto.MailArticleDto;
import com.example.onlinenews.main_article.entity.MainArticle;
import com.example.onlinenews.main_article.service.MainArticleService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class MailingService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private MainArticleService mainArticleService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(fromEmail);
        javaMailSender.send(message);
    }

    public void setHtmlMail(String to) throws MessagingException {
        List<MailArticleDto> headArticles = getHeadArticlesByCategory();
        String emailContent = generateEmailTemplate(headArticles);

        sendHtmlMail(to, "Today's Headlines", emailContent);

    }

    private void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private List<MailArticleDto> getHeadArticlesByCategory() {
        List<MainArticle> articles = mainArticleService.getHeadArticlesForToday();

        List<MailArticleDto> mailArticles = new ArrayList<>();
        for (MainArticle article : articles) {
            Article articleData = article.getArticle();

            String firstArticleImg = null;
            if (!articleData.getImages().isEmpty()) {
                firstArticleImg = articleData.getImages().get(0).getImgUrl();
            }
            MailArticleDto mailArticle = new MailArticleDto(articleData.getId(), articleData.getCategory(),
                    articleData.getTitle(), articleData.getSubtitle(), articleData.getViews(),
                    articleData.getUser().getName() + " 기자", extractPreview(articleData.getContent()),
                    article.getPublisher().getName(), firstArticleImg);
            mailArticles.add(mailArticle);
        }

        return mailArticles;
    }

    private String generateEmailTemplate(List<MailArticleDto> articles) {
        Context context = new Context();
        context.setVariable("articles", articles);  // articles 변수로 템플릿에 전달

        System.out.println("기사들 : " + articles);
        return templateEngine.process("dailyNews", context);  // resources/templates/dailyNews.html
    }

    public static String extractPreview(String content) {
        String plainText = Jsoup.parse(content).text();

        return plainText;
    }
}
