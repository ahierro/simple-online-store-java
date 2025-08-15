package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.rowmappers.ProductRowMapper;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomProductRepositoryImpl implements CustomProductRepository{
    private final DatabaseClient db;
    private final ProductRowMapper productRowMapper;

    @Override
    public Mono<ProductDTO> findById(UUID id) {
        return db
                .sql("""
                        SELECT p.id as id_product,
                               p.name as product_name,
                               p.price as price,
                               p.stock as stock,
                               p.description as product_description,
                               p.big_image_url as big_image_url,
                               p.small_image_url as small_image_url,
                               c.name as category_name,
                               c.description as category_description,
                               c.created_at as category_created_at,
                               p.id_category as id_category,
                               p.created_at as created_at,
                               p.updated_at as updated_at
                        FROM product p INNER JOIN category c on c.id = p.id_category
                       where p.id = :id
                    """)
                .bind("id", id)
                .map(productRowMapper::apply)
                .one();
    }
}