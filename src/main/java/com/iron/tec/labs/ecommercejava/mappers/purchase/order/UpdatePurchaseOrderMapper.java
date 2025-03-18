package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UpdatePurchaseOrderMapper implements Converter<PurchaseOrderDTO, PurchaseOrder> {

    @Override
    public PurchaseOrder convert(@NonNull PurchaseOrderDTO source) {
        return PurchaseOrder.builder()
                .id(source.getId() != null ? UUID.fromString(source.getId()) : null)
                .idUser(source.getIdUser() != null ? UUID.fromString(source.getIdUser()) : null)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }
}