package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.dto.ProductUpdateDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductUpdateDTOToDomain implements Converter<ProductUpdateDTO, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductUpdateDTO source) {
        ProductDomain productDomain = new ProductDomain();
        productDomain.setName(source.getProductName());
        productDomain.setDescription(source.getProductDescription());
        productDomain.setStock(source.getStock());
        productDomain.setPrice(source.getPrice());
        productDomain.setSmallImageUrl(source.getSmallImageUrl());
        productDomain.setBigImageUrl(source.getBigImageUrl());
        productDomain.setDeleted(source.getDeleted() != null ? source.getDeleted() : false);
        productDomain.setIdCategory(UUID.fromString(source.getCategoryId()));
        return productDomain;
    }
}
