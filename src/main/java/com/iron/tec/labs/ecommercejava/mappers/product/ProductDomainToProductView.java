package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

public class ProductDomainToProductView implements Converter<ProductDomain, ProductView> {
    @Override
    public ProductView convert(@NonNull ProductDomain source) {
        return ProductView.builder()
                .id(source.getId())
                .productName(source.getName())
                .productDescription(source.getDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .idCategory(source.getIdCategory())
                .categoryName(null)
                .categoryDescription(null)
                .productCreatedAt(source.getCreatedAt())
                .deleted(source.getDeleted() != null ? source.getDeleted() : false)
                .build();
    }
}
