package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerConfirmTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should confirm user email with valid token")
    void confirmEmailValidToken() throws Exception {
        String validToken = "bfceecf0-95e2-4564-8516-2fea75d8fc63";
        String successMessage = "User inactiveuser confirmed!";

        mockMvc.perform(get("/api/confirm")
                        .param("token", validToken))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    @DisplayName("Should fail to confirm email with invalid token")
    void confirmEmailInvalidToken() throws Exception {
        String invalidToken = "6c21b4fa-870f-4d3d-9cec-623b8b8b9979";

        mockMvc.perform(get("/api/confirm")
                        .param("token", invalidToken))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fail to confirm email without token parameter")
    void confirmEmailNoToken() throws Exception {
        mockMvc.perform(get("/api/confirm"))
                .andExpect(status().isBadRequest());
    }
}
