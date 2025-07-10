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
class PurchaseOrderControllerDeleteTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should delete purchase order as admin")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deletePurchaseOrderAsAdmin() {
        testClient.delete()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus().isNoContent();

        // Verify the purchase order no longer exists
        testClient.get()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fail to delete purchase order as regular user")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void deletePurchaseOrderAsUser() {
        testClient.delete()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should handle deletion of non-existent purchase order")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteNonExistentPurchaseOrder() {
        testClient.delete()
                .uri("/api/purchase-order/99999999-0000-0000-0000-000000000000")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should require authentication to delete purchase order")
    void deletePurchaseOrderWithoutAuth() {
        testClient.delete()
                .uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
