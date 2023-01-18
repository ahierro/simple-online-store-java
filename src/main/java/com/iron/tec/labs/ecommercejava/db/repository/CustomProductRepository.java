package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomProductRepository {
    Mono<ProductDTO> findById(UUID id);

}
