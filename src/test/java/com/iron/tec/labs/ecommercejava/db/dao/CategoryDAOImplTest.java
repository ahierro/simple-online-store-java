package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.CategoryRepository;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.MethodName.class)
class CategoryDAOImplTest extends PostgresIntegrationSetup {

    @Container
    protected static PostgreSQLContainer<?> postgresqlContainer = createContainer();

    static {
        init(postgresqlContainer);
    }
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        overrideProperties(postgresqlContainer,registry);
    }

    @Autowired
    private CategoryDAOImpl categoryDAO;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    CategoryDomain exampleCategory = null;

    @BeforeEach
    public void setup() {
        categoryRepository.deleteAll();

        exampleCategory = CategoryDomain.builder()
                .id(UUID.fromString("9ea1e0f8-27ad-4915-9b49-9845b51f06d4"))
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
    }

    @Test
    @DisplayName("Create a category and find it to verify if it is persisted in the database")
    void create_ok() {
        categoryDAO.create(exampleCategory);
        assertEquals(1, categoryRepository.findAll().size());
    }

    @Test
    @DisplayName("Create a category that is already persisted in the database")
    void create_with_existing_id() {
        categoryDAO.create(exampleCategory);
        assertEquals(1, categoryRepository.findAll().size());
        
        try {
            categoryDAO.create(exampleCategory);
            Assertions.fail("Expected Conflict exception was not thrown");
        } catch (Conflict e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Update an existing category")
    void update_ok() {
        create_ok();
        assert exampleCategory.getId() != null;
        exampleCategory.setName("Motherboard");
        exampleCategory.setDescription("Description updated");
        assert exampleCategory.getId() != null;
        
        categoryDAO.update(exampleCategory);
        var category = categoryRepository.findById(exampleCategory.getId()).orElse(null);
        assertNotNull(category);
        assertTrue(isEquals(this.exampleCategory, category));
    }

    boolean isEquals(CategoryDomain categoryDomain, Category category){
        return categoryDomain.getId().equals(category.getId())
                && categoryDomain.getName().equals(category.getName())
                && categoryDomain.getDescription().equals(category.getDescription());
    }

    @Test
    @DisplayName("Update non-existing category")
    void update_nonExisting_error() {
        assert exampleCategory.getId() != null;
        exampleCategory.setCreatedAt(LocalDateTime.now());
        
        try {
            categoryDAO.update(exampleCategory);
            Assertions.fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Delete an existing category")
    void delete_existing_ok() {
        create_ok();
        assert exampleCategory.getId() != null;
        
        categoryDAO.delete(exampleCategory.getId().toString());
        assertTrue(categoryRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Delete a category associated to products should throw exception")
    void delete_with_conflict() {
        create_ok();
        
        // Get the saved category entity
        Category savedCategory = categoryRepository.findById(exampleCategory.getId()).orElse(null);
        assertNotNull(savedCategory);
        
        productRepository.save(Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .category(savedCategory)
                .build());
        productRepository.flush();
        assert exampleCategory.getId() != null;
        
        try {
            categoryDAO.delete(exampleCategory.getId().toString());
            Assertions.fail("Expected Conflict exception was not thrown");
        } catch (Conflict e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Delete a non-existing category")
    void delete_nonExisting_ok() {
        assert exampleCategory.getId() != null;
        
        try {
            categoryDAO.delete(exampleCategory.getId().toString());
            Assertions.fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }

    @Test
    @DisplayName("Get Page of categories when the table is not empty")
    void getPage_ok() {
        create_ok();
        PageDomain<CategoryDomain> page = categoryDAO.getPage(0, 2);
        assertNotNull(page);
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(1, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(1, page.getContent().size());
    }

    @Test
    @DisplayName("Get a category page when the table is empty")
    void getPage_empty() {
        PageDomain<CategoryDomain> page = categoryDAO.getPage(0, 2);
        assertNotNull(page);
        assertEquals(0, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(0, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(0, page.getContent().size());
    }

    @Test
    @DisplayName("Create a category and get category by id to verify if it is returned")
    void getById_ok() {
        create_ok();
        CategoryDomain category = categoryDAO.getById(exampleCategory.getId());
        assertNotNull(category);
    }

    @Test
    @DisplayName("Get non existing category")
    void getById_notFound() {
        try {
            categoryDAO.getById(UUID.randomUUID());
            Assertions.fail("Expected NotFound exception was not thrown");
        } catch (NotFound e) {
            // Expected exception
        }
    }
}
