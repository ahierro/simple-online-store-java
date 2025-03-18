package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
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
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
@Log4j2
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ConversionService conversionService;

    @Operation(summary = "Get page of categories",
            parameters = { @Parameter(in = ParameterIn.QUERY, name = "page", description = "Page"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Size") },
            responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryPage.class)))})
    @GetMapping("/page")
    public Mono<PageDomain<CategoryDTO>> getCategoriesPaged(@Valid PageRequestDTO pageRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before categories obtained")))
                .then(categoryService.getCategoryPage(pageRequest.getPage(), pageRequest.getSize())
                        .mapNotNull(page ->
                                new PageDomain<>(
                                        page.getContent().stream()
                                                .map(x -> conversionService.convert(x, CategoryDTO.class)).toList()
                                        , page.getTotalPages()
                                        , page.getTotalElements(),
                                          pageRequest.getPage()))
                )
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
                .then(categoryService.getById(id)
                        .mapNotNull(category -> conversionService.convert(category, CategoryDTO.class)))
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
                .then(categoryService.createCategory(conversionService.convert(categoryCreationDTO, CategoryDomain.class))
                        .mapNotNull(o -> conversionService.convert(o, CategoryDTO.class)))
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
        CategoryDomain categoryDomain = conversionService.convert(category, CategoryDomain.class);
        if (categoryDomain != null) {
            categoryDomain.setId(UUID.fromString(id));
        }

        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating category")))
                .then(categoryService.updateCategory(categoryDomain))
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
                .then(Mono.fromCallable(() -> ok().build()));
    }

}
