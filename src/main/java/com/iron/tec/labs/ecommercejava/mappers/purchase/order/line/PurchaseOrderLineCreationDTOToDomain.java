package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.domain.ProductDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseOrderLineCreationDTOToDomain implements Converter<@NonNull PurchaseOrderLineCreationDTO,@NonNull PurchaseOrderLineDomain> {
    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLineCreationDTO source) {
        return PurchaseOrderLineDomain.builder()
                .product(source.getIdProduct() != null ? 
                    ProductDomain.builder().id(UUID.fromString(source.getIdProduct())).build() : null)
                .quantity(source.getQuantity())
                .build();
    }
}
