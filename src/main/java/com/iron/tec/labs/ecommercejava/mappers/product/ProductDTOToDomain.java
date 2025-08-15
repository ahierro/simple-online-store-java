package com.iron.tec.labs.ecommercejava.mappers.product;

import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;

@Component
public class ProductDTOToDomain implements Converter<ProductDTO, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductDTO source) {
        CategoryDomain categoryDomain = null;
        if (source.getCategory() != null) {
            categoryDomain = CategoryDomain.builder()
                    .id(source.getCategory().getId() != null ? UUID.fromString(source.getCategory().getId()) : null)
                    .name(source.getCategory().getName())
                    .description(source.getCategory().getDescription())
                    .createdAt(source.getCategory().getCreatedAt())
                    .updatedAt(source.getCategory().getUpdatedAt())
                    .build();
        }

        return ProductDomain.builder()
                .id(source.getProductId() != null ? UUID.fromString(source.getProductId()) : null)
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .category(categoryDomain)
                .build();
    }
}
