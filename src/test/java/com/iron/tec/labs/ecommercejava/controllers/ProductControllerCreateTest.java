package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerCreateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should create product successfully as admin user")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createProductOk() throws Exception {
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "productId": "42ffafd7-e53e-4a88-9538-21968f3e1673",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/",
                      "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                    }
                    """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return forbidden when user is not admin")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void createProductForbidden() throws Exception {
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "productId": "42ffafd7-e53e-4a88-9538-21968f3e1673",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
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
    @DisplayName("Should return conflict when creating an existing product")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingProduct() throws Exception {
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/",
                      "categoryId": "61fef552-17b1-46ef-9452-48b93ad51022"
                    }
                    """))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return bad request when creating a product without mandatory fields")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingWithoutCategory() throws Exception {
        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                      "productName": "Gaming Mouse Xtreme",
                      "productDescription": "High DPI gaming mouse",
                      "stock": 0,
                      "price": 1,
                      "smallImageUrl": "https://www.youtube.com/",
                      "bigImageUrl": "https://www.youtube.com/"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

}
