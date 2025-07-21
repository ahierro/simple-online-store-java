package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;

import java.util.UUID;

public interface CategoryService {
    CategoryDomain getById(UUID id);
    CategoryDomain createCategory(CategoryDomain categoryCreationDTO);
    CategoryDomain updateCategory(CategoryDomain categoryCreationDTO);
    PageDomain<CategoryDomain> getCategoryPage(int page, int size);
    void deleteCategory(String id);
}
