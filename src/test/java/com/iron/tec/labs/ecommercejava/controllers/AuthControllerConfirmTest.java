package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerConfirmTest extends PostgresIntegrationSetup {

    @Autowired
    private WebTestClient testClient;

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    static {
        initWithScripts(postgresqlContainer, "scripts/authTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should confirm user email with valid token")
    void confirmEmailValidToken() {
        String validToken = "bfceecf0-95e2-4564-8516-2fea75d8fc63";
        String successMessage = "User inactiveuser confirmed!";

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/confirm")
                        .queryParam("token", validToken)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(successMessage);
    }

    @Test
    @DisplayName("Should fail to confirm email with invalid token")
    void confirmEmailInvalidToken() {
        String invalidToken = "6c21b4fa-870f-4d3d-9cec-623b8b8b9979";

        testClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/confirm")
                        .queryParam("token", invalidToken)
                        .build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fail to confirm email without token parameter")
    void confirmEmailNoToken() {
        testClient.get()
                .uri("/api/confirm")
                .exchange()
                .expectStatus().isBadRequest();
    }
}
