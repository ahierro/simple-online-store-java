package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, UUID> {

    @Modifying
    @Query("delete from product where id = :id")
    Mono<Integer> deleteProductById(UUID id);

    Flux<Product> findBy(Pageable pageable);
}
