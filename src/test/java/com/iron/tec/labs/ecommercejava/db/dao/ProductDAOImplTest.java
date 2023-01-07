package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.PostgresIntegrationSetup;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.exceptions.DuplicateKey;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.domain.PageImpl;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataR2dbcTest
class ProductDAOImplTest extends PostgresIntegrationSetup {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductRepository productRepository;

    Product product = null;

    @BeforeEach
    public void setup() {
        StepVerifier.create(productRepository.deleteAll()).verifyComplete();

        product = Product.builder()
                .id(UUID.randomUUID())
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
    }

    @Test
    @DisplayName("Create a product and find it to verify if it is persisted in the database")
    void create_ok() {
        StepVerifier.create(productDAO.create(product)
                        .thenMany(productRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a product that is already persisted in the database")
    void create_with_existing_id() {
        StepVerifier.create(productDAO.create(product)
                        .thenMany(productRepository.findAll()))
                .expectNextCount(1)
                .verifyComplete();
        product.setCreatedAt(null);
        StepVerifier.create(productDAO.create(product)
                        .thenMany(productRepository.findAll()))
                .verifyError(DuplicateKey.class);
    }

    @Test
    @DisplayName("Update an existing product")
    void update_ok() {
        create_ok();
        assert product.getId() != null;
        product.setName("Stove");
        product.setDescription("Description updated");
        product.setPrice(BigDecimal.ONE);
        product.setStock(15);
        product.setBigImageUrl("https://google.com/1.jpg");
        product.setSmallImageUrl("https://google.com/2.jpg");
        assert product.getId() != null;
        StepVerifier.create(productDAO.update(product)
                        .then(productRepository.findById(product.getId())))
                .expectNextMatches(product -> product != this.product
                        && product.equals(this.product))
                .verifyComplete();
    }

    @Test
    @DisplayName("Update non-existing product")
    void update_nonExisting_error() {
        assert product.getId() != null;
        product.setCreatedAt(LocalDateTime.now());
        StepVerifier.create(productDAO.update(product))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Delete an existing product")
    void delete_existing_ok() {
        create_ok();
        assert product.getId() != null;
        StepVerifier.create(productDAO.delete(product.getId().toString())
                        .then(productRepository.findById(product.getId())))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete a non-existing product")
    void delete_nonExisting_ok() {
        assert product.getId() != null;
        StepVerifier.create(productDAO.delete(product.getId().toString())
                        .then(productRepository.findById(product.getId())))
                .verifyError(NotFound.class);
    }

    @Test
    @DisplayName("Create a product and get all products to verify if it is returned")
    void getAll_ok() {
        create_ok();
        StepVerifier.create(productDAO.getAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create product and get a product page to verify if it is returned")
    void getPage_ok() {
        create_ok();
        PageImpl<Product> page = productDAO.getProductPage(0, 2).block();
        assertNotNull(page);
        assertEquals(1,page.getTotalPages());
        assertEquals(0,page.getNumber());
        assertEquals(1,page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(1,page.getContent().size());
    }
    @Test
    @DisplayName("Get a product page when the table is empty")
    void getPage_empty() {
        PageImpl<Product> page = productDAO.getProductPage(0, 2).block();
        assertNotNull(page);
        assertEquals(0,page.getTotalPages());
        assertEquals(0,page.getNumber());
        assertEquals(0,page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(0,page.getContent().size());
    }
    @Test
    @DisplayName("Get all products on an empty table to verify that it is not returned")
    void getAll_empty_ok() {
        StepVerifier.create(productDAO.getAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a product and get product by id to verify if it is returned")
    void getById_ok() {
        create_ok();
        StepVerifier.create(productDAO.getById(product.getId()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get non existing product")
    void getById_notFound() {
        StepVerifier.create(productDAO.getById(UUID.randomUUID()))
                .verifyError(NotFound.class);
    }
}
