package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.repository.UserRepository;
import com.iron.tec.labs.ecommercejava.dto.RegisterUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
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
    void confirmUserTest() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Mono.just(AppUser.builder()
                .id(UUID.randomUUID())
                .username("ahierro")
                .password("Pa$4_98452")
                .firstName("John")
                .lastName("Smith")
                .email("asd@gmail.com")
                .active(false)
                .build()
        ));
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

        String result = userServiceImpl.confirm(UUID.randomUUID().toString()).block();

        assertNotNull(result);
        verify(userRepository).save(any());
    }
}
