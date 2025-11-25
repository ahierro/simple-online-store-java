package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerGetTest extends PostgresIntegrationSetup {

    @Autowired
    private MockMvc mockMvc;

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    static {
        initWithScripts(postgresqlContainer, "scripts/categoryTests.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Test
    @DisplayName("Should return category by id for user role")
    void getByIdUser() throws Exception {
        mockMvc.perform(get("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                          "name": "Mouse",
                          "description": "Gamer Mouse"
                        }
                        """));
    }

    @Test
    @DisplayName("Should return category by id for admin role")
    void getByIdAdmin() throws Exception {
        mockMvc.perform(get("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                          "name": "Mouse",
                          "description": "Gamer Mouse"
                        }
                        """));
    }

    @Test
    @DisplayName("Should return not found for non-existent category id")
    void getByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/category/992968f5-42db-4284-bd86-1181f0890320")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paged categories for user role")
    void getCategoryPageUser() throws Exception {
        mockMvc.perform(get("/api/category/page?page=0&size=1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "content": [
                            {
                              "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                              "name": "Mouse",
                              "description": "Gamer Mouse"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """));
    }

    @Test
    @DisplayName("Should return paged categories for admin role")
    void getCategoryPageAdmin() throws Exception {
        mockMvc.perform(get("/api/category/page?page=0&size=1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "content": [
                            {
                              "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                              "name": "Mouse",
                              "description": "Gamer Mouse"
                            }
                          ],
                          "totalPages": 1,
                          "totalElements": 1,
                          "number": 0
                        }
                        """));
    }
}
