package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserNotificationServiceImpl implements UserNotificationService {
    private final EmailService emailService;
    private final String baseUrl;

    public UserNotificationServiceImpl(EmailService emailService,
                                      @Value("${app.base-url}") String baseUrl) {
        this.emailService = emailService;
        this.baseUrl = baseUrl;
    }

    @Override
    public void sendWelcomeEmail(AppUserDomain user) {
        log.info("Sending welcome email to user: {}", user.getUsername());
        emailService.sendEmailInParallel(
                user.getEmail(),
                "Welcome to EcommerceJava! - Please confirm your e-mail",
                buildWelcomeEmailContent(user)
        );
    }

    @Override
    public void sendUserConfirmationEmail(AppUserDomain user) {
        log.info("Sending confirmation email to user: {}", user.getUsername());
        emailService.sendEmailInParallel(
                user.getEmail(),
                "EcommerceJava - Account Confirmed",
                buildConfirmationEmailContent(user)
        );
    }

    private String buildWelcomeEmailContent(AppUserDomain user) {
        return String.format(
                "Welcome to EcommerceJava %s! Please confirm your e-mail by clicking on the following link: " +
                "<a href=\"%s/api/confirm?token=%s\">Confirm Mail!</a>",
                user.getFirstName(),
                baseUrl,
                user.getId()
        );
    }

    private String buildConfirmationEmailContent(AppUserDomain user) {
        return String.format(
                "Hello %s! Your account has been successfully confirmed. Welcome to EcommerceJava!",
                user.getFirstName()
        );
    }
}