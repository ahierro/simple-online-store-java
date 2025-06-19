package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderLineDomainToDTO implements Converter<PurchaseOrderLineDomain, PurchaseOrderLineDTO> {
    @Override
    public PurchaseOrderLineDTO convert(@NonNull PurchaseOrderLineDomain source) {
        return PurchaseOrderLineDTO.builder()
                .idProduct(source.getIdProduct() != null ? source.getIdProduct().toString() : null)
                .quantity(source.getQuantity())
                .build();
    }
}
