package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.ProductService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
@Log4j2
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;
    private final ConversionService conversionService;

    @Operation(summary = "Get page of products",
            parameters = { @Parameter(in = ParameterIn.QUERY, name = "page", description = "Page"),
                    @Parameter(in = ParameterIn.QUERY, name = "size", description = "Size") },
            responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductPage.class)))})
    @GetMapping("/page")
    public Mono<PageResponseDTO<ProductDTO>> getProductsPaged(@Valid ProductPageRequestDTO pageRequest, Authentication authentication) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before products obtained")))
                .then(productService.getProductPage(pageRequest.getPage(), pageRequest.getSize(),
                                ProductDomain.builder()
                                        .idCategory((pageRequest.getCategoryId() == null) ? null : UUID.fromString(pageRequest.getCategoryId()))
                                        .description(pageRequest.getQueryString())
                                        .deleted(pageRequest.getDeleted()).build(), authentication,pageRequest.getSortByPrice())
                        .mapNotNull(page ->
                                new PageResponseDTO<>(
                                        page.getContent().stream()
                                                .map(x -> conversionService.convert(x, ProductDTO.class)).toList(),
                                        PageRequest.of(page.getNumber(), pageRequest.getSize()),
                                        page.getTotalElements()
                                )
                        )
                )
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Products obtained")));
    }

    @Operation(summary = "Get Product Detail",
            responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @GetMapping("/{id}")
    public Mono<ProductDTO> getProduct(@PathVariable UUID id) {
        return productService.getById(id)
                .map(product -> conversionService.convert(product, ProductDTO.class));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Create Product", responses = {
            @ApiResponse(responseCode = "201",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource already exists", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @PostMapping()
    public Mono<ResponseEntity<Void>> createProduct(@RequestBody @Valid ProductCreationDTO productCreationDTO,
                                                    ServerHttpRequest serverHttpRequest) {
        ProductDomain productDomain = conversionService.convert(productCreationDTO, ProductDomain.class);
        return productService.createProduct(productDomain)
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product created")))
                .map(product -> ResponseEntity.created(
                        URI.create(serverHttpRequest.getPath().toString().concat("/")
                                .concat(productCreationDTO.getProductId()))).build());
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Update Product", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateProduct(@PathVariable("id") String id,
                                                    @RequestBody @Valid ProductUpdateDTO productUpdateDTO) {
        ProductDomain productDomain = conversionService.convert(productUpdateDTO, ProductDomain.class);
        if (productDomain != null) {
            productDomain.setId(UUID.fromString(id));
        }
        return productService.updateProduct(productDomain)
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product updated")))
                .map(x -> ok().build());
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") },
            summary = "Delete Product", responses = {
            @ApiResponse(responseCode = "200",description = "Successful Operation", content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication Failure", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)})
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("id") String id) {
        return productService.deleteProduct(id)
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product deleted")))
                .map(x -> ok().build());
    }

}
