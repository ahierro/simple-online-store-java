package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductDAO {
    Mono<ProductDomain> findById(UUID id);
    Mono<PageDomain<ProductDomain>> getProductViewPage(int page, int size, ProductDomain productDomainExample, Sort.Direction orderByPrice);
    Mono<ProductDomain> create(ProductDomain productDomain);
    Mono<ProductDomain> update(ProductDomain productDomain);
    Mono<Void> delete(String id);
}
