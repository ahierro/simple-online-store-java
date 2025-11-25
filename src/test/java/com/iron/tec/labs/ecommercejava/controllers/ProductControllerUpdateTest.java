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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerUpdateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should update product successfully as ADMIN")
    void updateProductOk() throws Exception {
        mockMvc.perform(put("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "productName": "updated6",
                           "productDescription": "updated",
                           "stock": 0,
                           "price": 1,
                           "smallImageUrl": "https://www.youtube.com/",
                           "bigImageUrl": "https://www.youtube.com/",
                           "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                         }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to update product")
    void updateProductForbidden() throws Exception {
        mockMvc.perform(put("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "productName": "updated6",
                           "productDescription": "updated",
                           "stock": 0,
                           "price": 1,
                           "smallImageUrl": "https://www.youtube.com/",
                           "bigImageUrl": "https://www.youtube.com/",
                           "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                         }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return NOT FOUND when updating a non-existing product")
    void updateProductNotFound() throws Exception {
        mockMvc.perform(put("/api/product/db8a41dd-6074-4a51-92a2-c138632da006")
                .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "productName": "updated6",
                           "productDescription": "updated",
                           "stock": 0,
                           "price": 1,
                           "smallImageUrl": "https://www.youtube.com/",
                           "bigImageUrl": "https://www.youtube.com/",
                           "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                         }
                        """))
                .andExpect(status().isNotFound());
    }
}
