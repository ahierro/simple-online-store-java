package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import com.iron.tec.labs.ecommercejava.dto.PageResponseDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

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

    @Value("classpath:json/product/responses/getAllResponse.json")
    Resource getAllResponse;

    @Value("classpath:json/product/responses/getPageResponse.json")
    Resource getProductPageResponse;

    @Value("classpath:json/product/responses/getByIdResponse.json")
    Resource getProductByIdResponse;

    @Value("classpath:json/product/requests/createRequest.json")
    Resource createProductRequest;

    @Value("classpath:json/product/requests/updateRequest.json")
    Resource updateProductRequest;

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getProductPage() {
        when(productService.getProductPage(any(PageRequestDTO.class))).thenReturn(Mono.just(new PageResponseDTO<>(
                Collections.singletonList(ProductDTO.builder()
                        .productId("f133d126-6cb1-4759-af63-9972f106a51d")
                        .productName("Test")
                        .stock(0)
                        .price(BigDecimal.valueOf(1))
                        .productDescription("Test2")
                        .smallImageUrl("https://github.com/1.jpg")
                        .bigImageUrl("https://github.com/2.jpg")
                        .createdAt(LocalDateTime.parse("2023-01-05T09:50:06.912024", DateTimeFormatter.ISO_DATE_TIME))
                        .build()), PageRequest.of(0, 1), 2)));
        testClient.get().uri("/v1/product/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getProductPageResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getById() {
        when(productService.getById(any(UUID.class))).thenReturn(Mono.just(ProductDTO.builder()
                .productId("baffc3a4-5447-48ab-b9c0-7604e448371d")
                .productName("Laptop")
                .stock(16)
                .price(BigDecimal.valueOf(123))
                .productDescription("Laptop 16gb RAM 500gb SDD CPU 8 cores")
                .smallImageUrl("https://github.com/1.jpg")
                .bigImageUrl("https://github.com/2.jpg")
                .build()));
        testClient.get().uri("/v1/product/baffc3a4-5447-48ab-b9c0-7604e448371d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getProductByIdResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createProduct() {

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
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingProduct() {

        when(productService.createProduct(any())).thenThrow(Conflict.class);

        testClient.post().uri("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createProductRequest))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateProduct() {

        when(productService.updateProduct(anyString(), any())).thenReturn(Mono.empty());

        testClient.put().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateProductRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateNonExistingProduct() {

        when(productService.updateProduct(anyString(), any())).thenThrow(NotFound.class);

        testClient.put().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateProductRequest))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteProduct() {

        when(productService.deleteProduct(anyString())).thenReturn(Mono.empty());

        testClient.delete().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteNonExistingProduct() {

        when(productService.deleteProduct(anyString())).thenThrow(NotFound.class);

        testClient.delete().uri("/v1/product/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
