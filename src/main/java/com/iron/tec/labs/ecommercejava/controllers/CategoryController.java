package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public PageDomain<CategoryDTO> getCategoriesPaged(@Valid PageRequestDTO pageRequest) {
        log.info("Before categories obtained");
        PageDomain<CategoryDomain> page = categoryService.getCategoryPage(pageRequest.getPage(), pageRequest.getSize());
        PageDomain<CategoryDTO> result = new PageDomain<>(
                page.getContent().stream()
                        .map(x -> conversionService.convert(x, CategoryDTO.class)).toList(),
                page.getTotalPages(),
                page.getTotalElements(),
                pageRequest.getPage());
        log.info("Categories obtained");
        return result;
    }

    @Operation(summary = "Get Category Detail", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public CategoryDTO getCategory(@PathVariable UUID id) {
        log.info("Before category obtained");
        CategoryDomain category = categoryService.getById(id);
        CategoryDTO result = conversionService.convert(category, CategoryDTO.class);
        log.info("Category obtained");
        return result;
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Create Category", responses = {
            @ApiResponse(responseCode = "201",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping()
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryCreationDTO categoryCreationDTO,
                                               HttpServletRequest httpServletRequest) {
        log.info("Before creating category");
        CategoryDomain categoryDomain = conversionService.convert(categoryCreationDTO, CategoryDomain.class);
        CategoryDomain createdCategory = categoryService.createCategory(categoryDomain);
        log.info("Category created");
        return ResponseEntity.created(
                URI.create(httpServletRequest.getRequestURI().concat("/")
                        .concat(categoryCreationDTO.getId()))).build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Update Category", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable("id") String id,
                                               @RequestBody @Valid CategoryUpdateDTO category) {
        CategoryDomain categoryDomain = conversionService.convert(category, CategoryDomain.class);
        if (categoryDomain != null) {
            categoryDomain.setId(UUID.fromString(id));
        }

        log.info("Before updating category");
        categoryService.updateCategory(categoryDomain);
        log.info("Category updated");
        return ok().build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Delete Category", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        log.info("Before deleting category");
        categoryService.deleteCategory(id);
        log.info("Category deleted");
        return ok().build();
    }

}
