package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductDTO> getById(UUID id);

    Mono<ProductDTO> createProduct(ProductCreationDTO productCreationDTO);

    Mono<ProductDTO> updateProduct(String id, ProductUpdateDTO productCreationDTO);

    Flux<ProductDTO> getAll();

    Mono<Void> deleteProduct(String id);
}
