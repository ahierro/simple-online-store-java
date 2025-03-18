package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryDAO {
    Mono<CategoryDomain> getById(UUID id);
    Mono<PageDomain<CategoryDomain>> getPage(int page, int size);
    Mono<CategoryDomain> create(CategoryDomain o);
    Mono<CategoryDomain> update(CategoryDomain o);
    Mono<Void> delete(String id);
}
