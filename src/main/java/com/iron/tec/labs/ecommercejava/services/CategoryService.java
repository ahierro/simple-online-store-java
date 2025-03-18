package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CategoryService {
    Mono<CategoryDomain> getById(UUID id);
    Mono<CategoryDomain> createCategory(CategoryDomain categoryCreationDTO);
    Mono<CategoryDomain> updateCategory(CategoryDomain categoryCreationDTO);
    Mono<PageDomain<CategoryDomain>> getCategoryPage(int page, int size);
    Mono<Void> deleteCategory(String id);
}
