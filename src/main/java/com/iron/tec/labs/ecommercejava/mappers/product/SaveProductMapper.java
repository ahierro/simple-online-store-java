package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SaveProductMapper implements Converter<ProductCreationDTO, Product> {

    @Override
    public Product convert(@NonNull ProductCreationDTO source) {
        return Product.builder()
                .id(source.getProductId() != null ? UUID.fromString(source.getProductId()) : null)
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .idCategory(UUID.fromString(source.getCategoryId()))
                .createdAt(LocalDateTime.now())
                .deleted(source.getDeleted() != null ? source.getDeleted() : false)
                .build();
    }
}