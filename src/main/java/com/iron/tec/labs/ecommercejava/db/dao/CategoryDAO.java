package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;

import java.util.UUID;

public interface CategoryDAO {
    CategoryDomain getById(UUID id);
    PageDomain<CategoryDomain> getPage(int page, int size);
    CategoryDomain create(CategoryDomain o);
    CategoryDomain update(CategoryDomain o);
    void delete(String id);
}
