package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UpdateProductMapper implements Converter<ProductUpdateDTO, Product> {

    @Override
    public Product convert(@NonNull ProductUpdateDTO source) {
        return Product.builder()
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .idCategory(UUID.fromString(source.getCategoryId()))
                .deleted(source.getDeleted() != null ? source.getDeleted() : false)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}