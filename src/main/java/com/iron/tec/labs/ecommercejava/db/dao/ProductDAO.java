package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface ProductDAO {
    ProductDomain findById(UUID id);
    PageDomain<ProductDomain> getProductViewPage(int page, int size, ProductDomain productDomainExample, Sort.Direction orderByPrice);
    ProductDomain create(ProductDomain productDomain);
    ProductDomain update(ProductDomain productDomain);
    void delete(String id);
}
