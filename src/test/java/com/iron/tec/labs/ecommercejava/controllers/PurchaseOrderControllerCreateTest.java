package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseOrderControllerCreateTest extends PostgresIntegrationSetup {

    @Autowired
    private WebTestClient testClient;

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    static {
        initWithScripts(postgresqlContainer, "scripts/purchaseOrderTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should create purchase order successfully as user")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void createPurchaseOrderAsUser() {
        testClient.post()
                .uri("/api/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "6394cc4c-1106-46a2-bc2a-44ec853c7150",
                          "lines": [
                            {
                              "idProduct": "4ebeb473-435b-428c-aa4a-914ae472bc45",
                              "quantity": 2
                            }
                          ],
                             "total": 1000.0
                        }
                        """)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @DisplayName("Should return forbidden admin user should not create purchase order")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void createPurchaseOrderAsAdmin() {
        testClient.post()
                .uri("/api/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "65a1b125-352c-4ce5-a84a-256df9faa940",
                          "lines": [
                            {
                              "idProduct": "4ebeb473-435b-428c-aa4a-914ae472bc45",
                              "quantity": 2
                            }
                          ],
                             "total": 1000.0
                        }
                        """)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should fail to create purchase order with invalid product")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void createPurchaseOrderWithInvalidProduct() {
        testClient.post()
                .uri("/api/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "34b14244-ee37-4655-9949-58de1198ba03",
                          "lines": [
                            {
                              "idProduct": "74c1b90f-d854-4f1d-9f56-a2d45f80201d",
                              "quantity": 2
                            }
                          ],
                             "total": 1000.0
                        }
                        """)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fail to create purchase order without authentication")
    void createPurchaseOrderWithoutAuth() {
        testClient.post()
                .uri("/api/purchase-order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "id": "55555555-eeee-8888-9452-48b93ad51022",
                          "lines": [
                            {
                              "idProduct": "a1b2c3d4-e5f6-4321-8765-abcdef123456",
                              "quantity": 2
                            }
                          ],
                             "total": 1000.0
                        }
                        """)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
