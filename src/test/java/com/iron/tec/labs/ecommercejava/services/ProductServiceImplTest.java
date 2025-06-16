package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductDAO productDAO;

    @Mock
    ConversionService conversionService;

    @Mock
    Authentication authentication;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void createProductTest() {
        UUID id = UUID.randomUUID();
        com.iron.tec.labs.ecommercejava.domain.ProductDomain productDomain = com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder()
                .id(id)
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        when(productDAO.create(any(com.iron.tec.labs.ecommercejava.domain.ProductDomain.class))).thenReturn(Mono.just(productDomain));
        com.iron.tec.labs.ecommercejava.domain.ProductDomain createdProduct = productService.createProduct(productDomain).block();
        assertNotNull(createdProduct);
        assertNotNull(productDomain.getId());
        assertEquals(productDomain.getId(), createdProduct.getId());
        verify(productDAO).create(any(com.iron.tec.labs.ecommercejava.domain.ProductDomain.class));
    }

    @Test
    void testDelete() {
        String id = UUID.randomUUID().toString();

        when(productDAO.delete(id)).thenReturn(Mono.empty());

        productService.deleteProduct(id).block();

        verify(productDAO).delete(id);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        com.iron.tec.labs.ecommercejava.domain.ProductDomain productDomain = com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder()
                .id(id)
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        when(productDAO.update(any(com.iron.tec.labs.ecommercejava.domain.ProductDomain.class))).thenReturn(Mono.empty());
        productService.updateProduct(productDomain).block();
        verify(productDAO, times(1)).update(any(com.iron.tec.labs.ecommercejava.domain.ProductDomain.class));
    }

    @Test
    void getProductPage() {
        com.iron.tec.labs.ecommercejava.domain.ProductDomain filter = com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder().build();
        when(productDAO.getProductViewPage(eq(0), eq(1), eq(filter), isNull())).thenReturn(Mono.just(
                new com.iron.tec.labs.ecommercejava.domain.PageDomain<>(
                        Arrays.asList(
                                com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder()
                                        .id(UUID.randomUUID())
                                        .name("Laptop")
                                        .stock(16)
                                        .price(BigDecimal.valueOf(123))
                                        .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                                        .smallImageUrl("https://github.com/1.jpg")
                                        .bigImageUrl("https://github.com/2.jpg")
                                        .build(),
                                com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder()
                                        .id(UUID.randomUUID())
                                        .name("Laptop")
                                        .stock(16)
                                        .price(BigDecimal.valueOf(123))
                                        .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                                        .smallImageUrl("https://github.com/1.jpg")
                                        .bigImageUrl("https://github.com/2.jpg")
                                        .build()
                        ), 2, 2, 0)));
        com.iron.tec.labs.ecommercejava.domain.PageDomain<com.iron.tec.labs.ecommercejava.domain.ProductDomain> page = productService.getProductPage(0, 1, filter, null, null).block();
        assertNotNull(page);
        assertEquals(2, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertEquals(2, page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(2, page.getContent().size());
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        com.iron.tec.labs.ecommercejava.domain.ProductDomain productDomain = com.iron.tec.labs.ecommercejava.domain.ProductDomain.builder()
                .id(id)
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        when(productDAO.findById(any(UUID.class))).thenReturn(Mono.just(productDomain));
        StepVerifier.create(productService.getById(id))
                .expectNextCount(1)
                .verifyComplete();
    }
}
