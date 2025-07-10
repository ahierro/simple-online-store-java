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
class PurchaseOrderControllerGetTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should return purchase order by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdUser() {
        testClient.get().uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo("22222222-bbbb-5555-9452-48b93ad51022")
                .jsonPath("$.userId").isEqualTo("11111111-aaaa-4444-9452-48b93ad51022")
                .jsonPath("$.totalAmount").isEqualTo(149.98)
                .jsonPath("$.status").isEqualTo("CREATED")
                .jsonPath("$.lines.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("Should return purchase order by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getByIdAdmin() {
        testClient.get().uri("/api/purchase-order/22222222-bbbb-5555-9452-48b93ad51022")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.id").isEqualTo("22222222-bbbb-5555-9452-48b93ad51022")
                .jsonPath("$.userId").isEqualTo("11111111-aaaa-4444-9452-48b93ad51022")
                .jsonPath("$.totalAmount").isEqualTo(149.98)
                .jsonPath("$.status").isEqualTo("CREATED")
                .jsonPath("$.lines.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("Should return not found for non-existent purchase order id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdNotFound() {
        testClient.get().uri("/api/purchase-order/99999999-0000-0000-0000-000000000000")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return paged purchase orders for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getPurchaseOrderPageUser() {
        testClient.get().uri("/api/purchase-order/page?page=0&size=10")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo("22222222-bbbb-5555-9452-48b93ad51022")
                .jsonPath("$.content[0].userId").isEqualTo("11111111-aaaa-4444-9452-48b93ad51022")
                .jsonPath("$.content[0].totalAmount").isEqualTo(149.98)
                .jsonPath("$.content[0].status").isEqualTo("CREATED")
                .jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    @DisplayName("Should return paged purchase orders for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getPurchaseOrderPageAdmin() {
        testClient.get().uri("/api/purchase-order/page?page=0&size=10")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo("22222222-bbbb-5555-9452-48b93ad51022")
                .jsonPath("$.content[0].userId").isEqualTo("11111111-aaaa-4444-9452-48b93ad51022")
                .jsonPath("$.content[0].totalAmount").isEqualTo(149.98)
                .jsonPath("$.content[0].status").isEqualTo("CREATED")
                .jsonPath("$.totalElements").isEqualTo(1);
    }
}
