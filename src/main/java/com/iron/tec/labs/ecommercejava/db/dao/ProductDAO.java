package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDAO {
    Flux<Product> getAll();
    Mono<Product> create(Product product);
    Mono<Product> update(Product product);
    Mono<Void> delete(String id);
}
