package com.iron.tec.labs.ecommercejava.controllers;

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

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerDeleteTest extends PostgresIntegrationSetup {

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
        initWithScripts(postgresqlContainer, "scripts/productTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should delete product successfully as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteProductOk() {
        testClient.delete().uri("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to delete product")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void deleteProductForbidden() {
        testClient.delete().uri("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should return NOT FOUND when deleting non-existent product as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteProductNotFound() {
        testClient.get().uri("/api/product/2aad9592-b85f-4b9a-83f9-38d0a54f3a96")
                .exchange()
                .expectStatus().isNotFound();
    }
}
