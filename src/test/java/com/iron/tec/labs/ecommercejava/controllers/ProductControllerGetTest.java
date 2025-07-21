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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerGetTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should return product by id for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductByIdUser() throws Exception {
        mockMvc.perform(get("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                          "productName": "Gaming Mouse",
                          "productDescription": "High precision gaming mouse",
                          "stock": 100,
                          "price": 59.99,
                          "smallImageUrl": "https://example.com/small.jpg",
                          "bigImageUrl": "https://example.com/big.jpg",
                          "createdAt": "2025-06-20T14:48:23.453723"
                        }
                        """));
    }

    @Test
    @DisplayName("Should return product by id for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getProductByIdAdmin() throws Exception {
        mockMvc.perform(get("/api/product/a8f01033-6f92-4ef5-9437-7d54738c9b1a"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                          "productName": "Gaming Mouse",
                          "productDescription": "High precision gaming mouse",
                          "stock": 100,
                          "price": 59.99,
                          "smallImageUrl": "https://example.com/small.jpg",
                          "bigImageUrl": "https://example.com/big.jpg",
                          "createdAt": "2025-06-20T14:48:23.453723"
                        }
                        """));
    }

    @Test
    @DisplayName("Should return not found for non-existent product id")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/product/992968f5-42db-4284-bd86-1181f0890320"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paged products for user role")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductPageUser() throws Exception {
        mockMvc.perform(get("/api/product/page?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "content": [
                            {
                              "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                              "productName": "Gaming Mouse",
                              "productDescription": "High precision gaming mouse",
                              "stock": 100,
                              "price": 59.99,
                              "smallImageUrl": "https://example.com/small.jpg",
                              "bigImageUrl": "https://example.com/big.jpg",
                              "createdAt": "2025-06-20T14:48:23.453723"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """));
    }

    @Test
    @DisplayName("Should return paged products for admin role")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void getProductPageAdmin() throws Exception {
        mockMvc.perform(get("/api/product/page?page=0&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "content": [
                            {
                              "productId": "a8f01033-6f92-4ef5-9437-7d54738c9b1a",
                              "productName": "Gaming Mouse",
                              "productDescription": "High precision gaming mouse",
                              "stock": 100,
                              "price": 59.99,
                              "smallImageUrl": "https://example.com/small.jpg",
                              "bigImageUrl": "https://example.com/big.jpg",
                              "createdAt": "2025-06-20T14:48:23.453723"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """));
    }
}
