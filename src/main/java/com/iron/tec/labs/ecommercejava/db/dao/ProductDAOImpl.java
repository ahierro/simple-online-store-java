package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.db.repository.ProductRepository;
import com.iron.tec.labs.ecommercejava.exceptions.DuplicateKey;
import com.iron.tec.labs.ecommercejava.exceptions.NotFound;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@AllArgsConstructor
@Transactional
@Log4j2
public class ProductDAOImpl implements ProductDAO {
    private final ProductRepository productRepository;

    @Override
    public Flux<Product> getAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Mono<Product> create(Product product) {
        return productRepository.save(product)
                .doOnError(DataIntegrityViolationException.class, e -> {
            throw new DuplicateKey("Product already exists");
        });
    }

    @Override
    public Mono<Product> update(Product product) {
        return productRepository.save(product)
                .doOnError(TransientDataAccessResourceException.class, e -> {
                    throw new NotFound("Product not found");
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.productRepository.deleteProductById(UUID.fromString(id)).flatMap(x -> {
                    if (x == 0) return Mono.error(new NotFound("Product not found"));
                    return Mono.empty();
                }
        );
    }
}
