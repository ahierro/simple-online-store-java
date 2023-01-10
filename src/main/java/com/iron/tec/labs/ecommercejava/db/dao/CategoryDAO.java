package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Category;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryDAO {
    Mono<Category> getById(UUID id);
    Flux<Category> getAll();

    Mono<PageImpl<Category>> getPage(int page, int size);

    Mono<Category> create(Category product);
    Mono<Category> update(Category product);
    Mono<Void> delete(String id);
}
