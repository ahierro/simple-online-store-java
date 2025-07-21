package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAO;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    @Override
    public CategoryDomain getById(UUID id) {
        return categoryDAO.getById(id);
    }

    public CategoryDomain createCategory(CategoryDomain category) {
        return categoryDAO.create(category);
    }

    @Override
    public CategoryDomain updateCategory(CategoryDomain category) {
        return categoryDAO.update(category);
    }

    @Override
    public PageDomain<CategoryDomain> getCategoryPage(int page, int size) {
        return categoryDAO.getPage(page, size);
    }

    @Override
    public void deleteCategory(String id) {
        categoryDAO.delete(id);
    }
}
