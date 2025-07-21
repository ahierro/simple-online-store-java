package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseOrderControllerGetTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should return purchase order by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getByIdUser() throws Exception {
        mockMvc.perform(get("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(jsonPath("$.total").value(149.98))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.lines.length()").value(2));
    }

    @Test
    @DisplayName("Should return purchase order by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getByIdAdmin() throws Exception {
        mockMvc.perform(get("/api/purchase-order/b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(jsonPath("$.total").value(149.98))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.lines.length()").value(2));
    }

    @Test
    @DisplayName("Should return not found for non-existent purchase order id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/purchase-order/95bf51e4-cf92-4936-9b7c-d1e990bca6ba"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paged purchase orders for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getPurchaseOrderPageUser() throws Exception {
        mockMvc.perform(get("/api/purchase-order/page?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(jsonPath("$.content[0].total").value(149.98))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return paged purchase orders for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN",username = "295ba273-ca1d-45bc-9818-f949223981f6")
    void getPurchaseOrderPageAdmin() throws Exception {
        mockMvc.perform(get("/api/purchase-order/page?page=0&size=10"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[0].id").value("b451dafd-7c96-43b6-bf5f-ac522dd3026c"))
                .andExpect(jsonPath("$.content[0].total").value(149.98))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
