package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.config.mail.config.MailConfig;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    JavaMailSender emailSender;
    @Mock
    MailConfig mailConfig;

    @Mock
    MimeMessage message;

    @InjectMocks
    EmailServiceImpl emailService;

    @Test
    @DisplayName("Should send email successfully")
    void sendEmailTest() {
        when(emailSender.createMimeMessage()).thenReturn(message);
        when(mailConfig.getUsername()).thenReturn("user@gmail.com");
        var result = emailService.sendMailSync("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when email sending fails")
    void sendEmailTestFailed() {
        when(emailSender.createMimeMessage()).thenReturn(message);
        when(mailConfig.getUsername()).thenReturn("user@gmail.com");
        doThrow(new RuntimeException("Error")).when(emailSender).send(any(MimeMessage.class));
        var result = emailService.sendMailSync("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
        assertFalse(result);
    }

    @Test
    @DisplayName("Should send email in parallel and return result")
    void sendEmailInParallel() {
        emailService.sendEmailInParallel("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
    }
}