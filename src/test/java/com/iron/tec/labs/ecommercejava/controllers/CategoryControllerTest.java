package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.config.ConverterConfig;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.exceptions.Conflict;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import com.iron.tec.labs.ecommercejava.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(CategoryController.class)
@Import(ConverterConfig.class)
class CategoryControllerTest {
    @Autowired
    WebTestClient testClient;

    @MockitoBean
    CategoryService categoryService;

    @Value("classpath:json/category/responses/getPageResponse.json")
    Resource getCategoryPageResponse;

    @Value("classpath:json/category/responses/getByIdResponse.json")
    Resource getCategoryByIdResponse;

    @Value("classpath:json/category/requests/createRequest.json")
    Resource createCategoryRequest;

    @Value("classpath:json/category/requests/updateRequest.json")
    Resource updateCategoryRequest;

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getCategoryPage() {
        when(categoryService.getCategoryPage(anyInt(), anyInt())).thenReturn(Mono.just(new PageDomain<>(
                Collections.singletonList(CategoryDomain.builder()
                        .id(UUID.fromString("f133d126-6cb1-4759-af63-9972f106a51d"))
                        .name("Test")
                        .description("Test2")
                        .createdAt(LocalDateTime.parse("2023-01-05T09:50:06.912024", DateTimeFormatter.ISO_DATE_TIME))
                        .build()), 2, 2, 0)));
        testClient.get().uri("/api/category/page?page=0&size=1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getCategoryPageResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_USER")
    void getById() {
        when(categoryService.getById(any(UUID.class))).thenReturn(Mono.just(CategoryDomain.builder()
                .id(UUID.fromString("baffc3a4-5447-48ab-b9c0-7604e448371d"))
                .name("Motherboards")
                .description("Motherboards")
                .build()));
        testClient.get().uri("/api/category/baffc3a4-5447-48ab-b9c0-7604e448371d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .json(ResourceUtils.readFile(getCategoryByIdResponse));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createCategory() {

        when(categoryService.createCategory(any())).thenReturn(Mono.just(CategoryDomain.builder()
                .id(UUID.fromString("63466fc5-dccd-43c2-a3c7-4028bd9684bb"))
                .name("Motherboards")
                .description("Motherboards")
                .build()));

        testClient.post().uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createCategoryRequest))
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void createExistingCategory() {

        when(categoryService.createCategory(any())).thenThrow(Conflict.class);

        testClient.post().uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(createCategoryRequest))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateCategory() {

        when(categoryService.updateCategory(any())).thenReturn(Mono.empty());

        testClient.put().uri("/api/category/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateCategoryRequest))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void updateNonExistingCategory() {

        when(categoryService.updateCategory(any())).thenThrow(NotFound.class);

        testClient.put().uri("/api/category/f833d126-6cb1-4759-af63-9972f106a51d")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResourceUtils.readFile(updateCategoryRequest))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteCategory() {

        when(categoryService.deleteCategory(anyString())).thenReturn(Mono.empty());

        testClient.delete().uri("/api/category/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    @WithMockUser(authorities = "SCOPE_ROLE_ADMIN")
    void deleteNonExistingCategory() {

        when(categoryService.deleteCategory(anyString())).thenThrow(NotFound.class);

        testClient.delete().uri("/api/category/f833d126-6cb1-4759-af63-9972f106a51d")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

}
