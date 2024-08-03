package com.pricecomparison.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);
    private final static String FAILED_TO_SEND_EMAIL_MSG = "Failed to send email";
    private final JavaMailSender mailSender;

    @Async
    public void send(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setFrom("noreply@interia.pl");
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error(FAILED_TO_SEND_EMAIL_MSG, e);
            throw new IllegalStateException(FAILED_TO_SEND_EMAIL_MSG);
        }
    }
}
