package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ProductService {
    ProductDomain getById(UUID id);
    ProductDomain createProduct(ProductDomain productDomain);
    ProductDomain updateProduct(ProductDomain productDomain);
    PageDomain<ProductDomain> getProductPage(int page, int size, ProductDomain example, Authentication authentication, Sort.Direction  sortBy);
    void deleteProduct(String id);
}
