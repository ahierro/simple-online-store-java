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
class CategoryControllerCreateTest extends PostgresIntegrationSetup {

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
    @DisplayName("Should create category successfully as admin user")
    void createCategoryOk() throws Exception {
        mockMvc.perform(post("/api/category")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "f833d126-6cb1-4759-af63-9972f106a51d",
                                  "name": "Motherboards",
                                  "description": "Motherboards"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return forbidden when user is not admin")
    void createCategoryForbidden() throws Exception {
        mockMvc.perform(post("/api/category")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "a94c47ad-5a17-44b8-b3c7-22127ea8ac31",
                                  "name": "Test",
                                  "description": "Test Description"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return conflict when creating an existing category")
    void createExistingCategory() throws Exception {
        mockMvc.perform(post("/api/category")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": "61fef552-17b1-46ef-9452-48b93ad51022",
                                  "name": "Motherboards",
                                  "description": "Motherboards"
                                }
                                """))
                .andExpect(status().isConflict());
    }

}
