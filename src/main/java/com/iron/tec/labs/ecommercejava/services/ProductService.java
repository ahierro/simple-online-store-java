package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductDTO> getById(UUID id);

    Mono<ProductDTO> createProduct(ProductCreationDTO productCreationDTO);

    Mono<ProductDTO> updateProduct(String id, ProductUpdateDTO productCreationDTO);

    Flux<ProductDTO> getAll();
    Mono<PageResponseDTO<ProductDTO>> getProductPage(PageRequestDTO pageRequest);

    Mono<Void> deleteProduct(String id);
}
