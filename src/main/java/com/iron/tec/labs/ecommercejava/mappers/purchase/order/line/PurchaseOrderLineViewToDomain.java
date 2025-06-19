package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderLineViewToDomain implements Converter<PurchaseOrderLineView, PurchaseOrderLineDomain> {
    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLineView source) {
        return PurchaseOrderLineDomain.builder()
                .id(source.getId())
                .idPurchaseOrder(source.getIdPurchaseOrder())
                .idProduct(source.getIdProduct())
                .quantity(source.getQuantity())
                .build();
    }
}
