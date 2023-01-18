package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductDAO {

    Mono<ProductDTO> findById(UUID id);
    Mono<PageImpl<Product>> getProductPage(int page, int size);

    Mono<PageImpl<ProductView>> getProductViewPage(int page, int size);


    Mono<Product> create(Product product);
    Mono<Product> update(Product product);
    Mono<Void> delete(String id);
}
