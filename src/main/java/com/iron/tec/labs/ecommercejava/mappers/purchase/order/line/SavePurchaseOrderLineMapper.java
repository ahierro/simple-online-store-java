package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SavePurchaseOrderLineMapper implements Converter<PurchaseOrderLineCreationDTO, PurchaseOrderLine> {

    @Override
    public PurchaseOrderLine convert(@NonNull PurchaseOrderLineCreationDTO source) {
        return PurchaseOrderLine.builder()
                .id(UUID.randomUUID())
                .idProduct(UUID.fromString(source.getIdProduct()))
                .idPurchaseOrder(UUID.randomUUID())
                .quantity(source.getQuantity())
                .build();
    }
}