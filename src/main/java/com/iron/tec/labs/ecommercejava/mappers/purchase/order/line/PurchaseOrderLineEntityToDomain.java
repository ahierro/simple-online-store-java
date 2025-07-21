package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.mappers.product.ProductEntityToDomain;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PurchaseOrderLineEntityToDomain implements Converter<PurchaseOrderLine, PurchaseOrderLineDomain> {

    private final ProductEntityToDomain productEntityToDomain;

    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLine source) {
        return PurchaseOrderLineDomain.builder()
                .id(source.getId())
                .product(source.getProduct() != null ? productEntityToDomain.convert(source.getProduct()) : null)
                .quantity(source.getQuantity())
                .build();
    }
}
