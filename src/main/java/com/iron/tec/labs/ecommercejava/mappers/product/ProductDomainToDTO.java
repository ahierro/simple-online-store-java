package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainToDTO implements Converter<@NonNull ProductDomain,@NonNull ProductDTO> {
    @Override
    public ProductDTO convert(@NonNull ProductDomain source) {
        CategoryDTO categoryDTO = null;
        if (source.getCategory() != null) {
            categoryDTO = CategoryDTO.builder()
                    .id(source.getCategory().getId() != null ? source.getCategory().getId().toString() : null)
                    .name(source.getCategory().getName())
                    .description(source.getCategory().getDescription())
                    .createdAt(source.getCategory().getCreatedAt())
                    .updatedAt(source.getCategory().getUpdatedAt())
                    .build();
        }

        return ProductDTO.builder()
                .productId(source.getId() != null ? source.getId().toString() : null)
                .productName(source.getName())
                .productDescription(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .category(categoryDTO)
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
