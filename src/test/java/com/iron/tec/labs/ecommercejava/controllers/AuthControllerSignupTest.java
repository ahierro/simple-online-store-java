package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerSignupTest extends PostgresIntegrationSetup {

    @Autowired
    private MockMvc mockMvc;

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
    void signupSuccess() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "newuser",
                          "email": "newuser@example.com",
                          "password": "password123",
                          "firstName": "New",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isOk());

        // Verify we cannot immediately login (since email confirmation is required)
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "newuser",
                          "password": "password123"
                        }
                        """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should fail signup with existing username")
    void signupFailExistingUsername() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "testuser",
                          "email": "unique@example.com",
                          "password": "password123",
                          "firstName": "Unique",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should fail signup with existing email")
    void signupFailExistingEmail() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "uniqueuser",
                          "email": "user@example.com",
                          "password": "password123",
                          "firstName": "Unique",
                          "lastName": "User"
                        }
                        """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should fail signup with invalid email format")
    void signupFailInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "badmailuser",
                          "email": "not-an-email",
                          "password": "password123",
                          "firstName": "Bad",
                          "lastName": "Email"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail signup with missing required fields")
    void signupFailMissingFields() throws Exception {
        mockMvc.perform(post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "incompleteuser",
                          "email": "incomplete@example.com"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
