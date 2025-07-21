package com.iron.tec.labs.ecommercejava.mappers.product;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;

@Component
public class ProductViewToProductDomain implements Converter<ProductView, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductView source) {
        CategoryDomain categoryDomain = null;
        if (source.getIdCategory() != null) {
            categoryDomain = CategoryDomain.builder()
                    .id(source.getIdCategory())
                    .name(source.getCategoryName())
                    .description(source.getCategoryDescription())
                    .createdAt(source.getCategoryCreatedAt())
                    .updatedAt(source.getCategoryUpdatedAt())
                    .build();
        }

        return ProductDomain.builder()
                .id(source.getId())
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .category(categoryDomain)
                .createdAt(source.getProductCreatedAt())
                .build();
    }
}
