package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseOrderControllerCreateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should create purchase order successfully as user")
    void createPurchaseOrderAsUser() throws Exception {
        mockMvc.perform(post("/api/purchase-order")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .claim("sub", "295ba273-ca1d-45bc-9818-f949223981f6")
                                        .claim("preferred_username", "295ba273-ca1d-45bc-9818-f949223981f6")
                                )
                                .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return forbidden admin user should not create purchase order")
    void createPurchaseOrderAsAdmin() throws Exception {
        mockMvc.perform(post("/api/purchase-order")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should fail to create purchase order with invalid product")
    void createPurchaseOrderWithInvalidProduct() throws Exception {
        mockMvc.perform(post("/api/purchase-order")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .claim("sub", "295ba273-ca1d-45bc-9818-f949223981f6")
                                        .claim("preferred_username", "295ba273-ca1d-45bc-9818-f949223981f6")
                                )
                                .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "34b14244-ee37-4655-9949-58de1198ba03",
                                  "lines": [
                                    {
                                      "idProduct": "ff33ec48-0b26-42d8-8909-666b88147d79",
                                      "quantity": 2
                                    }
                                  ],
                                     "total": 1000.0
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should fail to create purchase order without authentication")
    void createPurchaseOrderWithoutAuth() throws Exception {
        mockMvc.perform(post("/api/purchase-order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return conflict when creating a purchase order with existing ID")
    void createDuplicatePurchaseOrder() throws Exception {
        mockMvc.perform(post("/api/purchase-order")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .claim("sub", "295ba273-ca1d-45bc-9818-f949223981f6")
                                        .claim("preferred_username", "295ba273-ca1d-45bc-9818-f949223981f6")
                                )
                                .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "b451dafd-7c96-43b6-bf5f-ac522dd3026c",
                                  "lines": [
                                    {
                                      "idProduct": "4ebeb473-435b-428c-aa4a-914ae472bc45",
                                      "quantity": 1
                                    }
                                  ],
                                  "total": 89.99
                                }
                                """))
                .andExpect(status().isConflict());
    }
}
