package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetProductViewMapper implements Converter<ProductView, ProductDTO> {

    @Override
    public ProductDTO convert(@NonNull ProductView source) {
        CategoryDTO category = CategoryDTO.builder()
                .id(source.getIdCategory().toString())
                .name(source.getCategoryName())
                .description(source.getCategoryDescription())
                .createdAt(source.getCategoryCreatedAt())
                .build();

        return ProductDTO.builder()
                .productId(source.getId().toString())
                .productName(source.getProductName())
                .productDescription(source.getProductDescription())
                .price(source.getPrice())
                .createdAt(source.getProductCreatedAt())
                .category(category)
                .build();
    }
}