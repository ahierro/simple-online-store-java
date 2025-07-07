package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EmailService emailService;
    @Mock
    MessageService messageService;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Should create a user successfully")
    void createUserTest() {
        when(userRepository.save(any(AppUser.class))).thenReturn(Mono.just(AppUser.builder()
                .id(UUID.randomUUID())
                .username("ahierro")
                .password("Pa$4_98452")
                .firstName("John")
                .lastName("Smith")
                .email("asd@gmail.com")
                .active(false)
                .authority("ROLE_USER")
                .build()));
        when(passwordEncoder.encode(any())).thenReturn(UUID.randomUUID().toString());

        userServiceImpl.create(
                RegisterUserDTO.builder()
                        .username("ahierro")
                        .password("Pa$4_98452")
                        .email("asd@gmail.com")
                        .firstName("John")
                        .lastName("Smith")
                        .build()).block();

        verify(userRepository).save(any());
        verify(passwordEncoder).encode(any());
        verify(emailService).sendEmailInParallel(any(), any(), any());
    }

    @Test
    @DisplayName("Should confirm user successfully")
    void confirmUserTest() {
        when(userRepository.findByIdAndActive(any(UUID.class), anyBoolean())).thenReturn(Mono.just(AppUser.builder()
                .id(UUID.randomUUID())
                .username("ahierro")
                .password("Pa$4_98452")
                .firstName("John")
                .lastName("Smith")
                .email("asd@gmail.com")
                .active(false)
                .build()
        ));
        when(userRepository.save(any())).thenReturn(Mono.just(AppUser.builder()
                .id(UUID.randomUUID())
                .username("ahierro")
                .password("Pa$4_98452")
                .firstName("John")
                .lastName("Smith")
                .email("asd@gmail.com")
                .active(false)
                .authority("ROLE_USER")
                .build()));

        String result = userServiceImpl.confirm(UUID.randomUUID().toString()).block();

        assertNotNull(result);
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("Should throw NotFound when confirming a non-existent user")
    void confirmUserTestFail() {
        when(userRepository.findByIdAndActive(any(UUID.class), anyBoolean())).thenReturn(Mono.empty());

        StepVerifier.create(userServiceImpl.confirm(UUID.randomUUID().toString()))
                .verifyErrorMatches(throwable -> throwable instanceof NotFound);
    }
}
