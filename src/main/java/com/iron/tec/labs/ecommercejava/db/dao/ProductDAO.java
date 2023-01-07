package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductDAO {
    Mono<Product> getById(UUID id);
    Flux<Product> getAll();

    Mono<PageImpl<Product>> getProductPage(int page, int size);

    Mono<Product> create(Product product);
    Mono<Product> update(Product product);
    Mono<Void> delete(String id);
}
