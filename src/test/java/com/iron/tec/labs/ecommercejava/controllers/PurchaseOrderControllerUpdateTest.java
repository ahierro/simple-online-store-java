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
class PurchaseOrderControllerUpdateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should update purchase order status as admin")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updatePurchaseOrderAsAdmin() {
        testClient.patch()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "SHIPPED"
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("SHIPPED");
    }

    @Test
    @DisplayName("Should fail to update purchase order as regular user")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void updatePurchaseOrderAsUser() {
        testClient.patch()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "SHIPPED"
                        }
                        """)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should fail to update non-existent purchase order")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateNonExistentPurchaseOrder() {
        testClient.patch()
                .uri("/api/purchase-order/99999999-0000-0000-0000-000000000000")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "SHIPPED"
                        }
                        """)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fail to update purchase order with invalid status")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updatePurchaseOrderWithInvalidStatus() {
        testClient.patch()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "INVALID_STATUS"
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
