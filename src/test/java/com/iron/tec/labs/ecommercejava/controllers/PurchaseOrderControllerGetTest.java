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
class PurchaseOrderControllerGetTest extends PostgresIntegrationSetup {

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
        initWithScripts(postgresqlContainer, "scripts/purchaseOrderTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should return purchase order by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getByIdUser() {
        testClient.get().uri("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .jsonPath("$.total").isEqualTo(149.98)
                .jsonPath("$.status").isEqualTo("PENDING")
                .jsonPath("$.lines.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("Should return purchase order by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getByIdAdmin() {
        testClient.get().uri("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .jsonPath("$.total").isEqualTo(149.98)
                .jsonPath("$.status").isEqualTo("PENDING")
                .jsonPath("$.lines.length()").isEqualTo(2);
    }

    @Test
    @DisplayName("Should return not found for non-existent purchase order id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdNotFound() {
        testClient.get().uri("/api/purchase-order/95bf51e4-cf92-4936-9b7c-d1e990bca6ba")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return paged purchase orders for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getPurchaseOrderPageUser() {
        testClient.get().uri("/api/purchase-order/page?page=0&size=10")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo("b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .jsonPath("$.content[0].total").isEqualTo(149.98)
                .jsonPath("$.content[0].status").isEqualTo("PENDING")
                .jsonPath("$.totalElements").isEqualTo(1);
    }

    @Test
    @DisplayName("Should return paged purchase orders for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getPurchaseOrderPageAdmin() {
        testClient.get().uri("/api/purchase-order/page?page=0&size=10")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.content[0].id").isEqualTo("b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .jsonPath("$.content[0].total").isEqualTo(149.98)
                .jsonPath("$.content[0].status").isEqualTo("PENDING")
                .jsonPath("$.totalElements").isEqualTo(1);
    }
}
