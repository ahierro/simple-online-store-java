package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerSignupTest extends PostgresIntegrationSetup {

    private WebTestClient testClient;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUpClient() {
        this.testClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

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
    @DisplayName("Should successfully register a new user")
    void signupSuccess() {
        testClient.post()
                .uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "newuser",
                          "email": "newuser@example.com",
                          "password": "password123",
                          "firstName": "New",
                          "lastName": "User"
                        }
                        """)
                .exchange()
                .expectStatus().isOk();

        // Verify we cannot immediately login (since email confirmation is required)
        testClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "newuser",
                          "password": "password123"
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Should fail signup with existing username")
    void signupFailExistingUsername() {
        testClient.post()
                .uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "testuser",
                          "email": "unique@example.com",
                          "password": "password123",
                          "firstName": "Unique",
                          "lastName": "User"
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Should fail signup with existing email")
    void signupFailExistingEmail() {
        testClient.post()
                .uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "uniqueuser",
                          "email": "user@example.com",
                          "password": "password123",
                          "firstName": "Unique",
                          "lastName": "User"
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Should fail signup with invalid email format")
    void signupFailInvalidEmail() {
        testClient.post()
                .uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "badmailuser",
                          "email": "not-an-email",
                          "password": "password123",
                          "firstName": "Bad",
                          "lastName": "Email"
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Should fail signup with missing required fields")
    void signupFailMissingFields() {
        testClient.post()
                .uri("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "username": "incompleteuser",
                          "email": "incomplete@example.com"
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
