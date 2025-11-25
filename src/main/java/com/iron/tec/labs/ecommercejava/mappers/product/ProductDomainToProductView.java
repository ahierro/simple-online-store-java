package com.iron.tec.labs.ecommercejava.mappers.product;

import com.iron.tec.labs.ecommercejava.db.entities.ProductView;
import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainToProductView implements Converter<@NonNull ProductDomain,@NonNull ProductView> {
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
                .idCategory(source.getCategory() != null ? source.getCategory().getId() : null)
                .categoryName(source.getCategory() != null ? source.getCategory().getName() : null)
                .categoryDescription(source.getCategory() != null ? source.getCategory().getDescription() : null)
                .productCreatedAt(source.getCreatedAt())
                .categoryCreatedAt(source.getCategory() != null ? source.getCategory().getCreatedAt() : null)
                .build();
    }
}
