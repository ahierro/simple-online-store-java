package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerGetTest extends PostgresIntegrationSetup {

    @Autowired
    private WebTestClient testClient;

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
    @DisplayName("Should return product by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductByIdUser() {
        testClient.get().uri("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                          "productName": "Gaming Mouse",
                          "productDescription": "High precision gaming mouse",
                          "stock": 100,
                          "price": 59.99,
                          "smallImageUrl": "https://example.com/small.jpg",
                          "bigImageUrl": "https://example.com/big.jpg",
                          "createdAt": "2025-06-20T14:48:23.453723"
                        }
                        """);
    }

    @Test
    @DisplayName("Should return product by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getProductByIdAdmin() {
        testClient.get().uri("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                          "productName": "Gaming Mouse",
                          "productDescription": "High precision gaming mouse",
                          "stock": 100,
                          "price": 59.99,
                          "smallImageUrl": "https://example.com/small.jpg",
                          "bigImageUrl": "https://example.com/big.jpg",
                          "createdAt": "2025-06-20T14:48:23.453723"
                        }
                        """);
    }

    @Test
    @DisplayName("Should return not found for non-existent product id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductByIdNotFound() {

        testClient.get().uri("/api/product/992968f5-42db-4284-bd86-1181f0890320")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return paged products for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductPageUser() {
        testClient.get().uri("/api/product/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "content": [
                            {
                              "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                              "productName": "Gaming Mouse",
                              "productDescription": "High precision gaming mouse",
                              "stock": 100,
                              "price": 59.99,
                              "smallImageUrl": "https://example.com/small.jpg",
                              "bigImageUrl": "https://example.com/big.jpg",
                              "createdAt": "2025-06-20T14:48:23.453723"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """);
    }

    @Test
    @DisplayName("Should return paged products for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getProductPageAdmin() {
        testClient.get().uri("/api/product/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json("""
                        {
                          "content": [
                            {
                              "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                              "productName": "Gaming Mouse",
                              "productDescription": "High precision gaming mouse",
                              "stock": 100,
                              "price": 59.99,
                              "smallImageUrl": "https://example.com/small.jpg",
                              "bigImageUrl": "https://example.com/big.jpg",
                              "createdAt": "2025-06-20T14:48:23.453723"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """);
    }
}
