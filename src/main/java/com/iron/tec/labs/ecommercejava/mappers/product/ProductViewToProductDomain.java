package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductViewToProductDomain implements Converter<ProductView, ProductDomain> {
    @Override
    public ProductDomain convert(@NonNull ProductView source) {
        return ProductDomain.builder()
                .id(source.getId())
                .name(source.getProductName())
                .description(source.getProductDescription())
                .stock(source.getStock())
                .price(source.getPrice())
                .smallImageUrl(source.getSmallImageUrl())
                .bigImageUrl(source.getBigImageUrl())
                .idCategory(source.getIdCategory())
                .categoryName(source.getCategoryName())
                .categoryDescription(source.getCategoryDescription())
                .categoryCreatedAt(source.getCategoryCreatedAt())
                .deleted(source.getDeleted())
                .createdAt(source.getProductCreatedAt())
                .build();
    }
}
