package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductService {
    Mono<ProductDomain> getById(UUID id);
    Mono<ProductDomain> createProduct(ProductDomain productDomain);
    Mono<ProductDomain> updateProduct(ProductDomain productDomain);
    Mono<PageDomain<ProductDomain>> getProductPage(int page, int size, ProductDomain example, Authentication authentication, Sort.Direction  sortBy);
    Mono<Void> deleteProduct(String id);
}
