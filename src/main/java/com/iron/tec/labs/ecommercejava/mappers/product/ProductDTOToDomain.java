package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductDTOToDomain implements Converter<ProductDTO, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductDTO source) {
        return ProductDomain.builder()
                .id(source.getProductId() != null ? UUID.fromString(source.getProductId()) : null)
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .createdAt(source.getCreatedAt())
                .deleted(source.getDeleted())
                // category mapping can be handled separately if needed
                .build();
    }
}
