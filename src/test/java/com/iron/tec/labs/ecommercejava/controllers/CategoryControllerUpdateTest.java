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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerUpdateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should update category successfully as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateCategoryOk() throws Exception {
        mockMvc.perform(put("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Keyboards",
                          "description": "Gamer Keyboards"
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to update category")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void updateCategoryForbidden() throws Exception {
        mockMvc.perform(put("/api/category/61fef552-17b1-46ef-9452-48b93ad51022")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Test",
                          "description": "Test Description"
                        }
                        """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return NOT FOUND when updating a non-existing category")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateCategoryNotFound() throws Exception {
        mockMvc.perform(put("/api/category/a94c47ad-5a17-44b8-b3c7-22127ea8ac31")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Keyboards",
                          "description": "Gamer Keyboards"
                        }
                        """))
                .andExpect(status().isNotFound());
    }
}
