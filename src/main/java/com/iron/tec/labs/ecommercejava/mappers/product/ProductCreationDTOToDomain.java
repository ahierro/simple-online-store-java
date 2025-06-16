package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductCreationDTOToDomain implements Converter<ProductCreationDTO, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductCreationDTO source) {
        ProductDomain productDomain = new ProductDomain();
        if (source.getProductId() != null) {
            productDomain.setId(UUID.fromString(source.getProductId()));
        }
        productDomain.setName(source.getProductName());
        productDomain.setDescription(source.getProductDescription());
        productDomain.setStock(source.getStock());
        productDomain.setPrice(source.getPrice());
        productDomain.setSmallImageUrl(source.getSmallImageUrl());
        productDomain.setBigImageUrl(source.getBigImageUrl());
        productDomain.setDeleted(source.getDeleted() != null ? source.getDeleted() : false);
        // category mapping can be handled separately if needed
        return productDomain;
    }
}
