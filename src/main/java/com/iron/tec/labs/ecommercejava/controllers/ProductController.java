package com.iron.tec.labs.ecommercejava.controllers;

import com.iron.tec.labs.ecommercejava.dto.*;
import com.iron.tec.labs.ecommercejava.services.ProductService;
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
@RequestMapping("/v1/product")
@Log4j2
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public Flux<ProductDTO> getProducts() {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before products obtained")))
                .thenMany(productService.getAll())
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Products obtained")));
    }

    @GetMapping("/page")
    public Mono<PageResponseDTO<ProductDTO>> getProductsPaged(@Valid PageRequestDTO pageRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before products obtained")))
                .then(productService.getProductPage(pageRequest))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Products obtained")));
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> getProduct(@PathVariable UUID id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before products obtained")))
                .then(productService.getById(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Products obtained")));
    }

    @PostMapping()
    public Mono<ResponseEntity<Void>> createProducts(@RequestBody @Valid ProductCreationDTO productCreationDTO,
                                                     ServerHttpRequest serverHttpRequest) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before creating product")))
                .then(productService.createProduct(productCreationDTO))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product created")))
                .map(product -> ResponseEntity.created(
                        URI.create(serverHttpRequest.getPath().toString().concat("/")
                                .concat(productCreationDTO.getProductId()))).build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateProducts(@PathVariable("id") String id,
                                                     @RequestBody @Valid ProductUpdateDTO product) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before updating product")))
                .then(productService.updateProduct(id, product))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product updated")))
                .map(x -> ok().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable("id") String id) {
        return Mono.empty()
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Before deleting product")))
                .then(productService.deleteProduct(id))
                .doOnEach(LoggingUtils.logOnComplete(x -> log.info("Product deleted")))
                .map(x -> ok().build());
    }

}
