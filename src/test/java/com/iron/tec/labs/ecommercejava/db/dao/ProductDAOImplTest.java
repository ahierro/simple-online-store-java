package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.CategoryRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductDAOImplTest extends PostgresIntegrationSetup {

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    static {
        init(postgresqlContainer);
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer, registry);
    }

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    MessageService messageService;

    Product product = null;
    Category category = null;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        category = Category.builder()
                .id(UUID.fromString("9ea1e0f8-27ad-4915-9b49-9845b51f06d4"))
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .category(category)
                .build();

        categoryRepository.save(category);
        assertEquals(1, categoryRepository.findAll().size());
    }

    private static ProductDomain toDomain(Product product) {
        CategoryDomain categoryDomain = null;
        if (product.getCategory() != null) {
            categoryDomain = CategoryDomain.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .description(product.getCategory().getDescription())
                    .createdAt(product.getCategory().getCreatedAt())
                    .build();
        }

        return ProductDomain.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .smallImageUrl(product.getSmallImageUrl())
                .bigImageUrl(product.getBigImageUrl())
                .category(categoryDomain)
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Test
    @DisplayName("Create a product and find it to verify if it is persisted in the database")
    void create_ok() {
        productDAO.create(toDomain(product));
        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    @DisplayName("Create a product that is already persisted in the database")
    void create_with_existing_id() {
        productDAO.create(toDomain(product));
        assertEquals(1, productRepository.findAll().size());
        
        try {
            productDAO.create(toDomain(product));
            fail("Expected Conflict exception was not thrown");
        } catch (Conflict e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Update an existing product")
    void update_ok() {
        create_ok();
        assert product.getId() != null;
        product.setName("Stove");
        product.setDescription("Description updated");
        product.setBigImageUrl("https://google.com/1.jpg");
        product.setSmallImageUrl("https://google.com/2.jpg");
        assert product.getId() != null;
        
        productDAO.update(toDomain(product));
        var updatedProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertNotSame(product, updatedProduct);
        assertEquals(product, updatedProduct);
    }

    @Test
    @DisplayName("Update non-existing product")
    void update_nonExisting_error() {
        assert product.getId() != null;
        product.setCreatedAt(LocalDateTime.now());
        
        try {
            productDAO.update(toDomain(product));
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Delete an existing product")
    void delete_existing_ok() {
        create_ok();
        assert product.getId() != null;
        
        productDAO.delete(product.getId().toString());
        assertTrue(productRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Delete a non-existing product")
    void delete_nonExisting_ok() {
        assert product.getId() != null;
        
        try {
            productDAO.delete(product.getId().toString());
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Create product and get a product page to verify if it is returned")
    void getPage_ok() {
        create_ok();
        com.iron.tec.labs.ecommercejava.domain.ProductDomain filter = toDomain(product);
        
        var page = productDAO.getProductViewPage(0, 2, filter, null);
        
        assertNotNull(page);
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(1, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(1, page.getContent().size());
    }

    @Test
    @DisplayName("Create a product and get product by id to verify if it is returned")
    void findById_ok() {
        create_ok();
        var foundProduct = productDAO.findById(product.getId());
        assertNotNull(foundProduct);
    }

    @Test
    @DisplayName("Get non existing product")
    void getById_notFound() {
        try {
            productDAO.findById(UUID.randomUUID());
            fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }
}
