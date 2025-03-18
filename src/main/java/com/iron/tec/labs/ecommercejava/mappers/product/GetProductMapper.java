package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetProductMapper implements Converter<Product, ProductDTO> {

    @Override
    public ProductDTO convert(@NonNull Product source) {
        return ProductDTO.builder()
                .productId(source.getId() != null ? source.getId().toString() : null)
                .productName(source.getName())
                .productDescription(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .deleted(source.getDeleted())
                .createdAt(source.getCreatedAt())
                .build();
    }
}