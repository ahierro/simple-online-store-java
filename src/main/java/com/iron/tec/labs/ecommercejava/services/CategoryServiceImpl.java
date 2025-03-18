package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAO;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    @Override
    public Mono<CategoryDomain> getById(UUID id) {
        return categoryDAO.getById(id);
    }

    public Mono<CategoryDomain> createCategory(CategoryDomain category) {
        return categoryDAO.create(category);
    }

    @Override
    public Mono<CategoryDomain> updateCategory(CategoryDomain category) {
        return categoryDAO.update(category);
    }

    @Override
    public Mono<PageDomain<CategoryDomain>> getCategoryPage(int page, int size) {
        return categoryDAO.getPage(page, size);
    }

    @Override
    public Mono<Void> deleteCategory(String id) {
        return categoryDAO.delete(id);
    }
}
