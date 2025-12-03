package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PurchaseOrderControllerPatchTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should update purchase order status as admin")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void updatePurchaseOrderAsAdmin() {
        testClient.patch()
                .uri("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "CANCELLED"
                        }
                        """)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Should fail to update purchase order as regular user")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void updatePurchaseOrderAsUser() {
        testClient.patch()
                .uri("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "CANCELLED"
                        }
                        """)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @DisplayName("Should fail to update non-existent purchase order")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void updateNonExistentPurchaseOrder() {
        testClient.patch()
                .uri("/api/purchase-order/e85d3f66-1e7a-4c12-bc98-04b031b4d80a")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "status": "CANCELLED"
                        }
                        """)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should fail to update purchase order with invalid status")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void updatePurchaseOrderWithInvalidStatus() {
        testClient.patch()
                .uri("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
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
