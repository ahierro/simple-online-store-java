package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderViewDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetPurchaseViewOrderMapper implements Converter<PurchaseOrderView, PurchaseOrderViewDTO> {

    @Override
    public PurchaseOrderViewDTO convert(@NonNull PurchaseOrderView source) {
        return PurchaseOrderViewDTO.builder()
                .id(source.getId().toString())
                .username(source.getUsername())
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .username(source.getUsername())
                .build();
    }
}