package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.dto.*;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductDTO> getById(UUID id);
    Mono<ProductDTO> createProduct(ProductCreationDTO productCreationDTO);
    Mono<ProductDTO> updateProduct(String id, ProductUpdateDTO productCreationDTO);
    Mono<PageResponseDTO<ProductDTO>> getProductPage(ProductPageRequestDTO pageRequest, Authentication authentication);
    Mono<Void> deleteProduct(String id);
}
