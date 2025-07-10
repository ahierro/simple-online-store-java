package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerLoginTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should login successfully with valid credentials as user")
    void loginSuccess() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "testuser",
                          "password": "admin"
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String token = response.getResponseBody();
                    assert token != null && !token.isEmpty();
                });
    }
    @Test
    @DisplayName("Should login successfully with valid credentials as admin")
    void loginAdminSuccess() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "adminuser",
                          "password": "adminpass"
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String token = response.getResponseBody();
                    assert token != null && !token.isEmpty();
                });
    }
    @Test
    @DisplayName("Should fail login with invalid password")
    void loginFailInvalidPassword() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "testuser",
                          "password": "wrongpassword"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Should fail login with non-existent username")
    void loginFailNonExistentUser() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "nonexistentuser",
                          "password": "admin"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Should fail login with inactive user")
    void loginFailInactiveUser() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "inactiveuser",
                          "password": "admin"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Should fail login with locked user")
    void loginFailLockedUser() {
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "lockeduser",
                          "password": "admin"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
