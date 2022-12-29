package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.ProductDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
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

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    void createProductTest() {
        UUID id = UUID.randomUUID();
        Product product = Product.builder()
                .id(id)
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();

        ProductCreationDTO productCreationDTO = ProductCreationDTO.builder()
                .productId(id.toString())
                .productName("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        ProductDTO productDTO = ProductDTO.builder()
                .productId(id.toString())
                .productName("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        when(conversionService.convert(any(ProductCreationDTO.class), eq(Product.class)))
                .thenReturn(product);
        when(conversionService.convert(any(Product.class), eq(ProductDTO.class)))
                .thenReturn(productDTO);
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        when(productDAO.create(argumentCaptor.capture())).thenReturn(Mono.just(product));
        ProductDTO createdProduct = productService.createProduct(productCreationDTO).block();

        assertNotNull(createdProduct);
        assertNotNull(product.getId());
        assertEquals(product.getId().toString(), createdProduct.getProductId());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(product, argumentCaptor.getValue());
        verify(productDAO).create(any(Product.class));
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
        Product product = Product.builder()
                .id(id)
                .name("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        ProductUpdateDTO productDTO = ProductUpdateDTO.builder()
                .productName("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build();
        ArgumentCaptor<Product> argumentCaptor = ArgumentCaptor.forClass(Product.class);
        when(conversionService.convert(any(ProductUpdateDTO.class), eq(Product.class)))
                .thenReturn(product);
        when(productDAO.update(argumentCaptor.capture())).thenReturn(Mono.empty());

        productService.updateProduct(id.toString(), productDTO).block();

        assertEquals(product, argumentCaptor.getValue());
        verify(productDAO, times(1)).update(any(Product.class));
    }

    @Test
    void testGetAll() {
        when(productDAO.getAll()).thenReturn(Flux.just(Product.builder()
                        .id(UUID.randomUUID())
                        .name("Laptop")
                        .stock(16)
                        .price(BigDecimal.valueOf(123))
                        .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("Laptop")
                        .stock(16)
                        .price(BigDecimal.valueOf(123))
                        .description("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .build()));

        when(conversionService.convert(any(Product.class), eq(ProductDTO.class)))
                .thenAnswer(x -> ProductDTO.builder()
                        .productId(UUID.randomUUID().toString())
                        .productName("Laptop")
                        .stock(16)
                        .price(BigDecimal.valueOf(123))
                        .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .build());

        StepVerifier.create(productService.getAll())
                .expectNextCount(2)
                .verifyComplete();

    }

}