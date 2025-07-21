package com.iron.tec.labs.ecommercejava.mappers.product;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;

@Component
public class ProductEntityToDomain implements Converter<Product, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull Product source) {
        CategoryDomain categoryDomain = null;
        if (source.getCategory() != null) {
            categoryDomain = CategoryDomain.builder()
                    .id(source.getCategory().getId())
                    .name(source.getCategory().getName())
                    .description(source.getCategory().getDescription())
                    .createdAt(source.getCategory().getCreatedAt())
                    .updatedAt(source.getCategory().getUpdatedAt())
                    .build();
        }

        return ProductDomain.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .category(categoryDomain)
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
