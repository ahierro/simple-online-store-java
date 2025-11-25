package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseOrderControllerPatchTest extends PostgresIntegrationSetup {

    @Autowired
    private MockMvc mockMvc;

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
    void updatePurchaseOrderAsAdmin() throws Exception {
        mockMvc.perform(patch("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "CANCELLED"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should fail to update purchase order as regular user")
    void updatePurchaseOrderAsUser() throws Exception {
        mockMvc.perform(patch("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "CANCELLED"
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail to update non-existent purchase order")
    void updateNonExistentPurchaseOrder() throws Exception {
        mockMvc.perform(patch("/api/purchase-order/e85d3f66-1e7a-4c12-bc98-04b031b4d80a")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "CANCELLED"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fail to update purchase order with invalid status")
    void updatePurchaseOrderWithInvalidStatus() throws Exception {
        mockMvc.perform(patch("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "status": "INVALID_STATUS"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
