package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerCreateTest extends PostgresIntegrationSetup {

    @Autowired
    private WebTestClient testClient;

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
    @DisplayName("Should create category successfully as admin user")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createCategoryOk() {

        testClient.post()
                .uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "f833d126-6cb1-4759-af63-9972f106a51d",
                          "name": "Motherboards",
                          "description": "Motherboards"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("Should return forbidden when user is not admin")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void createCategoryForbidden() {

        testClient.post()
                .uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "a94c47ad-5a17-44b8-b3c7-22127ea8ac31",
                          "name": "Test",
                          "description": "Test Description"
                        }
                        """)
                .exchange()
                .expectStatus().isForbidden();
    }


    @Test
    @DisplayName("Should return conflict when creating an existing category")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingCategory() {

        testClient.post()
                .uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                          "name": "Motherboards",
                          "description": "Motherboards"
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

    }

}
