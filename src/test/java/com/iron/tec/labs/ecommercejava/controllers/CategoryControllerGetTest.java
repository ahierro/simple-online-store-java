package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerGetTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should return category by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdUser() {

        testClient.get().uri("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                          "name": "Mouse",
                          "description": "Gamer Mouse"
                        }
                        """);
    }

    @Test
    @DisplayName("Should return category by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getByIdAdmin() {

        testClient.get().uri("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                          "name": "Mouse",
                          "description": "Gamer Mouse"
                        }
                        """);
    }

    @Test
    @DisplayName("Should return not found for non-existent category id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdNotFound() {

        testClient.get().uri("/api/category/992968f5-42db-4284-bd86-1181f0890320")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    @DisplayName("Should return paged categories for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getCategoryPageUser() {
        testClient.get().uri("/api/category/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "content": [
                            {
                              "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                              "name": "Mouse",
                              "description": "Gamer Mouse"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """);
    }

    @Test
    @DisplayName("Should return paged categories for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getCategoryPageAdmin() {
        testClient.get().uri("/api/category/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "content": [
                            {
                              "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                              "name": "Mouse",
                              "description": "Gamer Mouse"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """);
    }
}
