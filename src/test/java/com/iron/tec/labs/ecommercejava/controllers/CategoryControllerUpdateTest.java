package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerUpdateTest extends PostgresIntegrationSetup {

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
        initWithScripts(postgresqlContainer, "scripts/categoryTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should update category successfully as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateCategoryOk() {
        testClient.put().uri("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "name": "Keyboards",
                          "description": "Gamer Keyboards"
                        }
                        """)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to update category")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void updateCategoryForbidden() {

        testClient.put().uri("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "name": "Test",
                          "description": "Test Description"
                        }
                        """)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should return NOT FOUND when updating a non-existing category")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateCategoryNotFound() {
        testClient.put().uri("/api/category/a94c47ad-5a17-44b8-b3c7-22127ea8ac31")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "name": "Keyboards",
                          "description": "Gamer Keyboards"
                        }
                        """)
                .exchange()
                .expectStatus().isNotFound();
    }
}
