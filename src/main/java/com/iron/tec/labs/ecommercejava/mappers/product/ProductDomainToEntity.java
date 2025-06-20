package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.Product;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainToEntity implements Converter<ProductDomain, Product> {
    @Override
    public Product convert(@NonNull ProductDomain source) {
        Product product = new Product();
        product.setId(source.getId());
        product.setName(source.getName());
        product.setDescription(source.getDescription());
        product.setStock(source.getStock());
        product.setPrice(source.getPrice());
        product.setSmallImageUrl(source.getSmallImageUrl());
        product.setBigImageUrl(source.getBigImageUrl());
        product.setIdCategory(source.getIdCategory());
        product.setDeleted(source.getDeleted());
        product.setCreatedAt(source.getCreatedAt());
        return product;
    }
}
