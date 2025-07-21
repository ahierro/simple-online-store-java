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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBusinessServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserBusinessServiceImpl userBusinessService;

    @Test
    @DisplayName("Should prepare user for creation successfully")
    void prepareUserForCreation_success() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .username("testuser")
                .password("plainPassword")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .isAdmin(false)
                .build();

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        AppUserDomain user = userBusinessService.prepareUserForCreation(registerUserDTO);
        
        assertNotNull(user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertFalse(user.isActive());
        assertFalse(user.isLocked());
        assertEquals(List.of("ROLE_USER"), user.getAuthorities());
        assertNotNull(user.getCreatedAt());

        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    @DisplayName("Should prepare admin user for creation successfully")
    void prepareAdminUserForCreation_success() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .username("adminuser")
                .password("adminPassword")
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .isAdmin(true)
                .build();

        when(passwordEncoder.encode("adminPassword")).thenReturn("encodedAdminPassword");

        AppUserDomain user = userBusinessService.prepareUserForCreation(registerUserDTO);
        
        assertEquals("adminuser", user.getUsername());
        assertEquals("encodedAdminPassword", user.getPassword());
        assertEquals(List.of("ROLE_ADMIN"), user.getAuthorities());

        verify(passwordEncoder).encode("adminPassword");
    }

    @Test
    @DisplayName("Should activate user successfully")
    void activateUser_success() {
        UUID userId = UUID.randomUUID();
        AppUserDomain inactiveUser = AppUserDomain.builder()
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

        AppUserDomain activatedUser = AppUserDomain.builder()
                .id(userId)
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

        when(userDAO.findByIdAndActive(userId, false)).thenReturn(inactiveUser);
        when(userDAO.update(any(AppUserDomain.class))).thenReturn(activatedUser);

        AppUserDomain user = userBusinessService.activateUser(userId);
        
        assertEquals(userId, user.getId());
        assertTrue(user.isActive());
        assertNotNull(user.getUpdatedAt());

        verify(userDAO).findByIdAndActive(userId, false);
        verify(userDAO).update(any(AppUserDomain.class));
    }

    @Test
    @DisplayName("Should throw NotFound when trying to activate non-existent user")
    void activateUser_userNotFound() {
        UUID userId = UUID.randomUUID();

        when(userDAO.findByIdAndActive(userId, false)).thenThrow(new NotFound("User not found"));

        try {
            userBusinessService.activateUser(userId);
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }

        verify(userDAO).findByIdAndActive(userId, false);
        verify(userDAO, never()).update(any(AppUserDomain.class));
    }

    @Test
    @DisplayName("Should throw NotFound when trying to activate already active user")
    void activateUser_alreadyActive() {
        UUID userId = UUID.randomUUID();

        when(userDAO.findByIdAndActive(userId, false)).thenThrow(new NotFound("User not found"));

        try {
            userBusinessService.activateUser(userId);
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }

        verify(userDAO).findByIdAndActive(userId, false);
        verify(userDAO, never()).update(any(AppUserDomain.class));
    }
}