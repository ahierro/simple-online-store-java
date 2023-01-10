package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.CategoryService;
import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/category")
@Log4j2
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public Flux<CategoryDTO> getCategories() {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .thenMany(categoryService.getAll())
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Categories obtained")));
    }

    @GetMapping("/page")
    public Mono<PageResponseDTO<CategoryDTO>> getCategoriesPaged(@Valid PageRequestDTO pageRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .then(categoryService.getCategoryPage(pageRequest))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Categories obtained")));
    }

    @GetMapping("/{id}")
    public Mono<CategoryDTO> getCategory(@PathVariable UUID id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .then(categoryService.getById(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Categories obtained")));
    }

    @PostMapping()
    public Mono<ResponseEntity<Void>> createCategory(@RequestBody @Valid CategoryCreationDTO categoryCreationDTO,
                                                     ServerHttpRequest serverHttpRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before creating category")))
                .then(categoryService.createCategory(categoryCreationDTO))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Category created")))
                .map(category -> ResponseEntity.created(
                        URI.create(serverHttpRequest.getPath().toString().concat("/")
                                .concat(categoryCreationDTO.getId()))).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateCategory(@PathVariable("id") String id,
                                                     @RequestBody @Valid CategoryUpdateDTO category) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating category")))
                .then(categoryService.updateCategory(id, category))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Category updated")))
                .map(x -> ok().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable("id") String id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before deleting category")))
                .then(categoryService.deleteCategory(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Category deleted")))
                .map(x -> ok().build());
    }

}
