package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.config.mail.config.MailConfig;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final MailConfig mailConfig;

    @Override
    @Async
    public void sendEmailInParallel(String to, String subject, String body) {
        sendMailSync(to, subject, body);
    }

    @Override
    public Boolean sendMailSync(String to, String subject, String body) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setText(body, true);
            helper.setSubject(subject);
            helper.setFrom(mailConfig.getUsername());
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }
}
