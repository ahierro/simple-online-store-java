package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderLineDomainToEntity implements Converter<PurchaseOrderLineDomain, PurchaseOrderLine> {
    @Override
    public PurchaseOrderLine convert(@NonNull PurchaseOrderLineDomain source) {
        return PurchaseOrderLine.builder()
                .id(source.getId())
                .idPurchaseOrder(source.getIdPurchaseOrder())
                .idProduct(source.getIdProduct())
                .quantity(source.getQuantity())
                .build();
    }
}
