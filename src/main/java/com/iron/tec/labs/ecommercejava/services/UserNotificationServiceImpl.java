package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Log4j2
public class UserNotificationServiceImpl implements UserNotificationService {
    private final EmailService emailService;

    @Override
    public Mono<Void> sendWelcomeEmail(AppUserDomain user) {
        log.info("Sending welcome email to user: {}", user.getUsername());
        emailService.sendEmailInParallel(
                user.getEmail(),
                "Welcome to EcommerceJava! - Please confirm your e-mail",
                buildWelcomeEmailContent(user)
        );
        return Mono.empty();
    }

    @Override
    public Mono<Void> sendUserConfirmationEmail(AppUserDomain user) {
        log.info("Sending confirmation email to user: {}", user.getUsername());
        emailService.sendEmailInParallel(
                user.getEmail(),
                "EcommerceJava - Account Confirmed",
                buildConfirmationEmailContent(user)
        );
        return Mono.empty();
    }

    private String buildWelcomeEmailContent(AppUserDomain user) {
        return String.format(
                "Welcome to EcommerceJava %s! Please confirm your e-mail by clicking on the following link: " +
                "<a href=\"http://localhost:8080/api/confirm?token=%s\">Confirm Mail!</a>",
                user.getFirstName(),
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