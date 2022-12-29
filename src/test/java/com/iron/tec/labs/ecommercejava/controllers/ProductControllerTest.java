package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.exceptions.DuplicateKey;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    WebTestClient testClient;

    @MockBean
    ProductService productService;

    @Value("classpath:getAllResponse.json")
    Resource getAllResponse;

    @Value("classpath:createProductRequest.json")
    Resource createProductRequest;

    @Value("classpath:updateProductRequest.json")
    Resource updateProductRequest;

    @Test
    void getAll() {
        when(productService.getAll()).thenReturn(Flux.just(ProductDTO.builder()
                        .productId("baffc3a4-5447-48ab-b9c0-7604e448371d")
                        .productName("Laptop")
                        .stock(16)
                        .price(BigDecimal.valueOf(123))
                        .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .build(),
                ProductDTO.builder()
                        .productId("63466fc5-dccd-43c2-a3c7-4028bd9684bb")
                        .productName("Laptop")
                        .stock(16)
                        .price(BigDecimal.valueOf(100))
                        .productDescription("Laptop 4gb RAM 320gb SDD CPU 4 cores")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .build()));
        testClient.get().uri("/v1/product")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getAllResponse));
    }

    @Test
    void createProduct(){

        when(productService.createProduct(any())).thenReturn(Mono.just(ProductDTO.builder()
                .productId("63466fc5-dccd-43c2-a3c7-4028bd9684bb")
                .productName("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(100))
                .productDescription("Laptop 4gb RAM 320gb SDD CPU 4 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build()));

        testClient.post().uri("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createProductRequest))
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    void createExistingProduct(){

        when(productService.createProduct(any())).thenThrow(DuplicateKey.class);

        testClient.post().uri("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createProductRequest))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    void updateProduct(){

        when(productService.updateProduct(anyString(),any())).thenReturn(Mono.empty());

        testClient.put().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateProductRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

    }

    @Test
    void updateNonExistingProduct(){

        when(productService.updateProduct(anyString(),any())).thenThrow(NotFound.class);

        testClient.put().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateProductRequest))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteProduct(){

        when(productService.deleteProduct(anyString())).thenReturn(Mono.empty());

        testClient.delete().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    void deleteNonExistingProduct(){

        when(productService.deleteProduct(anyString())).thenThrow(NotFound.class);

        testClient.delete().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
