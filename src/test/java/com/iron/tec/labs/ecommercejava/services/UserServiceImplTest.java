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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void createUserTest() {
        when(userRepository.save(any(AppUser.class))).thenReturn(Mono.empty());
        when(passwordEncoder.encode(any())).thenReturn(UUID.randomUUID().toString());

        userServiceImpl.create(
                RegisterUserDTO.builder()
                .username("ahierro")
                .password("Pa$4_98452")
                .email("asd@gmail.com")
                .firstName("John")
                .firstName("Smith")
                .build()).block();

        verify(userRepository).save(any());
        verify(passwordEncoder).encode(any());
    }
}
