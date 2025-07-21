package com.iron.tec.labs.ecommercejava.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;

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
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isString())
                .andExpect(jsonPath("$.user.username").value("user"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.firstName").value("Test"))
                .andExpect(jsonPath("$.user.lastName").value("User"))
                .andExpect(jsonPath("$.lines.length()").value(2))
                .andExpect(jsonPath("$.lines[0].quantity").value(1))
                .andExpect(jsonPath("$.lines[0].product.productId").value("4ebeb473-435b-428c-aa4a-914ae472bc45"))
                .andExpect(jsonPath("$.lines[0].product.productName").value("Keyboard"))
                .andExpect(jsonPath("$.lines[0].product.productDescription").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.lines[0].product.stock").value(10))
                .andExpect(jsonPath("$.lines[0].product.price").value(89.99))
                .andExpect(jsonPath("$.lines[0].product.smallImageUrl").value("keyboard_small.jpg"))
                .andExpect(jsonPath("$.lines[0].product.bigImageUrl").value("keyboard_big.jpg"))
                .andExpect(jsonPath("$.lines[0].product.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[0].product.createdAt").isString())
                .andExpect(jsonPath("$.lines[0].product.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[0].product.category.id").value("345e0ef3-b8da-4a7e-ab5d-6636d63614b1"))
                .andExpect(jsonPath("$.lines[0].product.category.name").value("Hardware"))
                .andExpect(jsonPath("$.lines[0].product.category.description").value("Computer Hardware"))
                .andExpect(jsonPath("$.lines[0].product.category.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[0].product.category.createdAt").isString())
                .andExpect(jsonPath("$.lines[0].product.category.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[1].quantity").value(1))
                .andExpect(jsonPath("$.lines[1].product.productId").value("7486f916-1008-4611-98ef-746fe08197c6"))
                .andExpect(jsonPath("$.lines[1].product.productName").value("Mouse"))
                .andExpect(jsonPath("$.lines[1].product.productDescription").value("Gaming Mouse"))
                .andExpect(jsonPath("$.lines[1].product.stock").value(15))
                .andExpect(jsonPath("$.lines[1].product.price").value(59.99))
                .andExpect(jsonPath("$.lines[1].product.smallImageUrl").value("mouse_small.jpg"))
                .andExpect(jsonPath("$.lines[1].product.bigImageUrl").value("mouse_big.jpg"))
                .andExpect(jsonPath("$.lines[1].product.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[1].product.createdAt").isString())
                .andExpect(jsonPath("$.lines[1].product.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[1].product.category.id").value("345e0ef3-b8da-4a7e-ab5d-6636d63614b1"))
                .andExpect(jsonPath("$.lines[1].product.category.name").value("Hardware"))
                .andExpect(jsonPath("$.lines[1].product.category.description").value("Computer Hardware"))
                .andExpect(jsonPath("$.lines[1].product.category.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[1].product.category.createdAt").isString())
                .andExpect(jsonPath("$.lines[1].product.category.updatedAt").isEmpty());
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
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isString())
                .andExpect(jsonPath("$.user.username").value("user"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.firstName").value("Test"))
                .andExpect(jsonPath("$.user.lastName").value("User"))
                .andExpect(jsonPath("$.lines.length()").value(2))
                .andExpect(jsonPath("$.lines[0].quantity").value(1))
                .andExpect(jsonPath("$.lines[0].product.productId").value("4ebeb473-435b-428c-aa4a-914ae472bc45"))
                .andExpect(jsonPath("$.lines[0].product.productName").value("Keyboard"))
                .andExpect(jsonPath("$.lines[0].product.productDescription").value("Mechanical Keyboard"))
                .andExpect(jsonPath("$.lines[0].product.stock").value(10))
                .andExpect(jsonPath("$.lines[0].product.price").value(89.99))
                .andExpect(jsonPath("$.lines[0].product.smallImageUrl").value("keyboard_small.jpg"))
                .andExpect(jsonPath("$.lines[0].product.bigImageUrl").value("keyboard_big.jpg"))
                .andExpect(jsonPath("$.lines[0].product.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[0].product.createdAt").isString())
                .andExpect(jsonPath("$.lines[0].product.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[0].product.category.id").value("345e0ef3-b8da-4a7e-ab5d-6636d63614b1"))
                .andExpect(jsonPath("$.lines[0].product.category.name").value("Hardware"))
                .andExpect(jsonPath("$.lines[0].product.category.description").value("Computer Hardware"))
                .andExpect(jsonPath("$.lines[0].product.category.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[0].product.category.createdAt").isString())
                .andExpect(jsonPath("$.lines[0].product.category.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[1].quantity").value(1))
                .andExpect(jsonPath("$.lines[1].product.productId").value("7486f916-1008-4611-98ef-746fe08197c6"))
                .andExpect(jsonPath("$.lines[1].product.productName").value("Mouse"))
                .andExpect(jsonPath("$.lines[1].product.productDescription").value("Gaming Mouse"))
                .andExpect(jsonPath("$.lines[1].product.stock").value(15))
                .andExpect(jsonPath("$.lines[1].product.price").value(59.99))
                .andExpect(jsonPath("$.lines[1].product.smallImageUrl").value("mouse_small.jpg"))
                .andExpect(jsonPath("$.lines[1].product.bigImageUrl").value("mouse_big.jpg"))
                .andExpect(jsonPath("$.lines[1].product.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[1].product.createdAt").isString())
                .andExpect(jsonPath("$.lines[1].product.updatedAt").isEmpty())
                .andExpect(jsonPath("$.lines[1].product.category.id").value("345e0ef3-b8da-4a7e-ab5d-6636d63614b1"))
                .andExpect(jsonPath("$.lines[1].product.category.name").value("Hardware"))
                .andExpect(jsonPath("$.lines[1].product.category.description").value("Computer Hardware"))
                .andExpect(jsonPath("$.lines[1].product.category.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.lines[1].product.category.createdAt").isString())
                .andExpect(jsonPath("$.lines[1].product.category.updatedAt").isEmpty());
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
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].createdAt").isString())
                .andExpect(jsonPath("$.content[0].idUser").value("295ba273-ca1d-45bc-9818-f949223981f6"))
                .andExpect(jsonPath("$.content[0].username").value("user"))
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.content[0].firstName").value("Test"))
                .andExpect(jsonPath("$.content[0].lastName").value("User"))
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
                .andExpect(jsonPath("$.content[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$.content[0].createdAt").isString())
                .andExpect(jsonPath("$.content[0].idUser").value("295ba273-ca1d-45bc-9818-f949223981f6"))
                .andExpect(jsonPath("$.content[0].username").value("user"))
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.content[0].firstName").value("Test"))
                .andExpect(jsonPath("$.content[0].lastName").value("User"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
