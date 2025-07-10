package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.CategoryDTO;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainToDTO implements Converter<ProductDomain, ProductDTO> {
    @Override
    public ProductDTO convert(@NonNull ProductDomain source) {
        return ProductDTO.builder()
                .productId(source.getId() != null ? source.getId().toString() : null)
                .productName(source.getName())
                .productDescription(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .category(CategoryDTO.builder()
                        .id(source.getIdCategory() != null ? source.getIdCategory().toString() : null)
                        .name(source.getCategoryName())
                        .description(source.getCategoryDescription())
                        .createdAt(source.getCategoryCreatedAt())
                        .build())
                .deleted(source.getDeleted() != null ? source.getDeleted() : false)
                .createdAt(source.getCreatedAt())
                .build();
    }
}
