package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineCreationDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PurchaseOrderLineCreationDTOToDomain implements Converter<PurchaseOrderLineCreationDTO, PurchaseOrderLineDomain> {
    @Override
    public PurchaseOrderLineDomain convert(@NonNull PurchaseOrderLineCreationDTO source) {
        return PurchaseOrderLineDomain.builder()
                .id(UUID.randomUUID())
                .idProduct(source.getIdProduct() != null ? UUID.fromString(source.getIdProduct()) : null)
                .quantity(source.getQuantity())
                .build();
    }
}
