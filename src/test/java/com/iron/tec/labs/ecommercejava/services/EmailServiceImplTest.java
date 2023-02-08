package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.config.mail.config.MailConfig;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
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

    //test for sendEmail
    @Test
    void sendEmailTest() {
        when(emailSender.createMimeMessage()).thenReturn(message);
        when(mailConfig.getUsername()).thenReturn("user@gmail.com");
        var result = emailService.sendMailSync("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
        assertTrue(result);
    }

    @Test
    void sendEmailTestFailed() {
        when(emailSender.createMimeMessage()).thenReturn(message);
        when(mailConfig.getUsername()).thenReturn("user@gmail.com");
        doThrow(new RuntimeException("Error")).when(emailSender).send(any(MimeMessage.class));
        var result = emailService.sendMailSync("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
        assertFalse(result);
    }

    @Test
    void sendEmailInParallel() {
        var result = emailService.sendEmailInParallel("test@gmail.com", "Email Confirmation",
                "Confirm on the following link");
        assertNotNull(result);
    }
}