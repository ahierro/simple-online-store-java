package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.CategoryService;
import com.iron.tec.labs.ecommercejava.util.LoggingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/category")
@Log4j2
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get page of categories",
            parameters = { @Parameter(in = ParameterIn.QUERY, name = "page", description = "Page"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Size") },
            responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryPage.class)))})
    @GetMapping("/page")
    public Mono<PageResponseDTO<CategoryDTO>> getCategoriesPaged(@Valid PageRequestDTO pageRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .then(categoryService.getCategoryPage(pageRequest))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Categories obtained")));
    }

    @Operation(summary = "Get Category Detail", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public Mono<CategoryDTO> getCategory(@PathVariable UUID id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .then(categoryService.getById(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Categories obtained")));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Create Category", responses = {
            @ApiResponse(responseCode = "201",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
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

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Update Category", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateCategory(@PathVariable("id") String id,
                                                     @RequestBody @Valid CategoryUpdateDTO category) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating category")))
                .then(categoryService.updateCategory(id, category))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Category updated")))
                .map(x -> ok().build());
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Delete Category", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable("id") String id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before deleting category")))
                .then(categoryService.deleteCategory(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Category deleted")))
                .map(x -> ok().build());
    }

}
