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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerCreateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should create product successfully as admin user")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createProductOk() {
        testClient.post()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                      "productId": "42ffafd7-e53e-4a88-9538-21968f3e1673",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/",
                      "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                    }
                    """)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("Should return forbidden when user is not admin")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void createProductForbidden() {
        testClient.post()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                      "productId": "42ffafd7-e53e-4a88-9538-21968f3e1673",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/",
                      "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                    }
                    """)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should return conflict when creating an existing product")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingProduct() {
        testClient.post()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                      "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/",
                      "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                    }
                    """)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

    }

    @Test
    @DisplayName("Should return bad request when creating a product without mandatory fields")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingWithoutCategory() {
        testClient.post()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                      "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/"
                    }
                    """)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
