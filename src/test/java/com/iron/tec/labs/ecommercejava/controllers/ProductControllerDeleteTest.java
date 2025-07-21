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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerDeleteTest extends PostgresIntegrationSetup {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("Should delete product successfully as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteProductOk() throws Exception {
        mockMvc.perform(delete("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to delete product")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void deleteProductForbidden() throws Exception {
        mockMvc.perform(delete("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return NOT FOUND when deleting non-existent product as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteProductNotFound() throws Exception {
        mockMvc.perform(get("/api/product/2aad9592-b85f-4b9a-83f9-38d0a54f3a96"))
                .andExpect(status().isNotFound());
    }
}
