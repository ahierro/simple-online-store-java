package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.dto.ProductDTO;

import java.util.Optional;
import java.util.UUID;

public interface CustomProductRepository {
    Optional<ProductDTO> findById(UUID id);
}
