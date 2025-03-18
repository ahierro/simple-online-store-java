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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
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
        StepVerifier.create(productRepository.deleteAll()).verifyComplete();
        StepVerifier.create(categoryRepository.deleteAll()).verifyComplete();

        exampleCategory = CategoryDomain.builder()
                .id(UUID.fromString("9ea1e0f8-27ad-4915-9b49-9845b51f06d4"))
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
    }

    @Test
    @DisplayName("Create a category and find it to verify if it is persisted in the database")
    void create_ok() {
        StepVerifier.create(categoryDAO.create(exampleCategory)
                        .thenMany(categoryRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a category that is already persisted in the database")
    void create_with_existing_id() {
        StepVerifier.create(categoryDAO.create(exampleCategory)
                        .thenMany(categoryRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
        StepVerifier.create(categoryDAO.create(exampleCategory)
                        .thenMany(categoryRepository.findAll()))
                .verifyError(Conflict.class);
    }

    @Test
    @DisplayName("Update an existing category")
    void update_ok() {
        create_ok();
        assert exampleCategory.getId() != null;
        exampleCategory.setName("Motherboard");
        exampleCategory.setDescription("Description updated");
        exampleCategory.setDeleted(false);
        assert exampleCategory.getId() != null;
        StepVerifier.create(categoryDAO.update(exampleCategory)
                        .then(categoryRepository.findById(exampleCategory.getId())))
                .expectNextMatches(category -> isEquals(this.exampleCategory,category))
                .verifyComplete();
    }

    boolean isEquals(CategoryDomain categoryDomain, Category category){
        return categoryDomain.getId().equals(category.getId())
                && categoryDomain.getName().equals(category.getName())
                && categoryDomain.getDescription().equals(category.getDescription())
                && categoryDomain.getDeleted().equals(category.getDeleted());
    }

    @Test
    @DisplayName("Update non-existing category")
    void update_nonExisting_error() {
        assert exampleCategory.getId() != null;
        exampleCategory.setCreatedAt(LocalDateTime.now());
        StepVerifier.create(categoryDAO.update(exampleCategory))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Delete an existing category")
    void delete_existing_ok() {
        create_ok();
        assert exampleCategory.getId() != null;
        StepVerifier.create(categoryDAO.delete(exampleCategory.getId().toString())
                        .then(categoryRepository.findById(exampleCategory.getId())))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete a category associated to products should throw exception")
    void delete_with_conflict() {
        create_ok();
        productRepository.save(Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .idCategory(exampleCategory.getId())
                .build()).block();
        assert exampleCategory.getId() != null;
        StepVerifier.create(categoryDAO.delete(exampleCategory.getId().toString())
                        .then(categoryRepository.findById(exampleCategory.getId())))
                .verifyError(Conflict.class);
    }

    @Test
    @DisplayName("Delete a non-existing category")
    void delete_nonExisting_ok() {
        assert exampleCategory.getId() != null;
        StepVerifier.create(categoryDAO.delete(exampleCategory.getId().toString())
                        .then(categoryRepository.findById(exampleCategory.getId())))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Create category and get a category page to verify if it is returned")
    void getPage_ok() {
        create_ok();
        PageDomain<CategoryDomain> page = categoryDAO.getPage(0, 2).block();
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
        PageDomain<CategoryDomain> page = categoryDAO.getPage(0, 2).block();
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
        StepVerifier.create(categoryDAO.getById(exampleCategory.getId()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get non existing category")
    void getById_notFound() {
        StepVerifier.create(categoryDAO.getById(UUID.randomUUID()))
                .verifyError(NotFound.class);
    }
}
