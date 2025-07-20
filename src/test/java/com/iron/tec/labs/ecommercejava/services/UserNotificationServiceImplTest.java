package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceImplTest {

    @Mock
    private EmailService emailService;

    private UserNotificationServiceImpl userNotificationService;

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        userNotificationService = new UserNotificationServiceImpl(emailService, BASE_URL);
    }

    @Test
    @DisplayName("Should send welcome email successfully")
    void sendWelcomeEmail_success() {
        UUID userId = UUID.randomUUID();
        AppUserDomain user = AppUserDomain.builder()
                .id(userId)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .active(false)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        StepVerifier.create(userNotificationService.sendWelcomeEmail(user))
                .verifyComplete();

        verify(emailService).sendEmailInParallel(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                contentCaptor.capture()
        );

        assertEquals("test@example.com", emailCaptor.getValue());
        assertEquals("Welcome to EcommerceJava! - Please confirm your e-mail", subjectCaptor.getValue());
        assertTrue(contentCaptor.getValue().contains("Welcome to EcommerceJava John!"));
        assertTrue(contentCaptor.getValue().contains("confirm your e-mail"));
        assertTrue(contentCaptor.getValue().contains(userId.toString()));
    }

    @Test
    @DisplayName("Should send confirmation email successfully")
    void sendUserConfirmationEmail_success() {
        AppUserDomain user = AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .active(true)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        StepVerifier.create(userNotificationService.sendUserConfirmationEmail(user))
                .verifyComplete();

        verify(emailService).sendEmailInParallel(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                contentCaptor.capture()
        );

        assertEquals("test@example.com", emailCaptor.getValue());
        assertEquals("EcommerceJava - Account Confirmed", subjectCaptor.getValue());
        assertTrue(contentCaptor.getValue().contains("Hello John!"));
        assertTrue(contentCaptor.getValue().contains("successfully confirmed"));
        assertTrue(contentCaptor.getValue().contains("Welcome to EcommerceJava!"));
    }

    @Test
    @DisplayName("Should handle welcome email with different user names")
    void sendWelcomeEmail_differentNames() {
        AppUserDomain user = AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username("janesmith")
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .active(false)
                .build();

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        StepVerifier.create(userNotificationService.sendWelcomeEmail(user))
                .verifyComplete();

        verify(emailService).sendEmailInParallel(
                eq("jane@example.com"),
                anyString(),
                contentCaptor.capture()
        );

        assertTrue(contentCaptor.getValue().contains("Welcome to EcommerceJava Jane!"));
    }

    @Test
    @DisplayName("Should handle confirmation email with different user names")
    void sendUserConfirmationEmail_differentNames() {
        AppUserDomain user = AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username("janesmith")
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .active(true)
                .build();

        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        StepVerifier.create(userNotificationService.sendUserConfirmationEmail(user))
                .verifyComplete();

        verify(emailService).sendEmailInParallel(
                eq("jane@example.com"),
                anyString(),
                contentCaptor.capture()
        );

        assertTrue(contentCaptor.getValue().contains("Hello Jane!"));
    }
}