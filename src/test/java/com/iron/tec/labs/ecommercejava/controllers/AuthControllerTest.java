package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.services.JWTGeneratorService;
import com.iron.tec.labs.ecommercejava.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    WebTestClient testClient;

    @MockBean
    UserService userService;
    @MockBean
    JWTGeneratorService jwtGeneratorService;
    @MockBean
    ReactiveAuthenticationManager authenticationManager;

    @Value("classpath:json/user/loginRequest.json")
    Resource loginRequest;

    @Value("classpath:json/user/signUpRequest.json")
    Resource signUpRequest;

    @Test
    void login() {
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(authentication));
        when(jwtGeneratorService.generateToken(any())).thenReturn("token");

        testClient.post().uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(loginRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void signup() {

        when(userService.create(any())).thenReturn(Mono.empty());

        testClient.post().uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(signUpRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void confirm() {
        when(userService.confirm(any())).thenReturn(Mono.just("token"));

        testClient.get().uri("/api/confirm?token=token")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }
}
