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
class CategoryControllerDeleteTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should delete category successfully as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteCategoryOk() throws Exception {
        mockMvc.perform(delete("/api/category/61fef552-17b1-46ef-9452-48b93ad51022"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return FORBIDDEN when USER tries to delete category")
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void deleteCategoryForbidden() throws Exception {
        mockMvc.perform(delete("/api/category/61fef552-17b1-46ef-9452-48b93ad51022"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return NOT FOUND when deleting non-existent category as ADMIN")
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteCategoryNotFound() throws Exception {
        mockMvc.perform(get("/api/category/992968f5-42db-4284-bd86-1181f0890320"))
                .andExpect(status().isNotFound());
    }

}
