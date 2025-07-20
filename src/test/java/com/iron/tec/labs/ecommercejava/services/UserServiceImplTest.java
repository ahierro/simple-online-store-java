package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.UserDAO;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private UserBusinessService userBusinessService;

    @Mock
    private UserNotificationService userNotificationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create a user successfully")
    void createUserTest() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .isAdmin(false)
                .build();

        AppUserDomain preparedUser = AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .active(false)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        AppUserDomain createdUser = AppUserDomain.builder()
                .id(preparedUser.getId())
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .active(false)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        when(userBusinessService.prepareUserForCreation(registerUserDTO)).thenReturn(Mono.just(preparedUser));
        when(userDAO.create(preparedUser)).thenReturn(Mono.just(createdUser));
        when(userNotificationService.sendWelcomeEmail(createdUser)).thenReturn(Mono.empty());

        StepVerifier.create(userService.create(registerUserDTO))
                .verifyComplete();

        verify(userBusinessService).prepareUserForCreation(registerUserDTO);
        verify(userDAO).create(preparedUser);
        // Note: Email sending is fire-and-forget, so we can't verify it in the same way
    }

    @Test
    @DisplayName("Should confirm user successfully")
    void confirmUserTest() {
        UUID userId = UUID.randomUUID();
        String token = userId.toString();
        
        AppUserDomain activatedUser = AppUserDomain.builder()
                .id(userId)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .active(true)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userBusinessService.activateUser(userId)).thenReturn(Mono.just(activatedUser));
        when(userNotificationService.sendUserConfirmationEmail(activatedUser)).thenReturn(Mono.empty());

        StepVerifier.create(userService.confirm(token))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertEquals("User testuser confirmed!", result);
                    return true;
                })
                .verifyComplete();

        verify(userBusinessService).activateUser(userId);
        // Note: Email sending is fire-and-forget, so we can't verify it in the same way
    }

    @Test
    @DisplayName("Should throw NotFound when confirming a non-existent user")
    void confirmUserTestFail() {
        UUID userId = UUID.randomUUID();
        String token = userId.toString();

        when(userBusinessService.activateUser(userId)).thenReturn(Mono.error(new NotFound("User not found")));

        StepVerifier.create(userService.confirm(token))
                .verifyError(NotFound.class);

        verify(userBusinessService).activateUser(userId);
        verify(userNotificationService, never()).sendUserConfirmationEmail(any());
    }

    @Test
    @DisplayName("Should handle user creation when business service fails")
    void createUserTest_businessServiceFails() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .isAdmin(false)
                .build();

        when(userBusinessService.prepareUserForCreation(registerUserDTO))
                .thenReturn(Mono.error(new RuntimeException("Business logic error")));

        StepVerifier.create(userService.create(registerUserDTO))
                .verifyError(RuntimeException.class);

        verify(userBusinessService).prepareUserForCreation(registerUserDTO);
        verify(userDAO, never()).create(any());
        verify(userNotificationService, never()).sendWelcomeEmail(any());
    }

    @Test
    @DisplayName("Should handle user creation when DAO fails")
    void createUserTest_daoFails() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .isAdmin(false)
                .build();

        AppUserDomain preparedUser = AppUserDomain.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("John")
                .lastName("Smith")
                .active(false)
                .locked(false)
                .authorities(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        when(userBusinessService.prepareUserForCreation(registerUserDTO)).thenReturn(Mono.just(preparedUser));
        when(userDAO.create(preparedUser)).thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(userService.create(registerUserDTO))
                .verifyError(RuntimeException.class);

        verify(userBusinessService).prepareUserForCreation(registerUserDTO);
        verify(userDAO).create(preparedUser);
        verify(userNotificationService, never()).sendWelcomeEmail(any());
    }
}
