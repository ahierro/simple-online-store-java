package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.db.entities.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityToDomain implements Converter<Product, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull Product source) {
        return ProductDomain.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .idCategory(source.getIdCategory())
                .deleted(source.getDeleted())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
