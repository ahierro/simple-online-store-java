package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderDomainToView implements Converter<PurchaseOrderDomain, PurchaseOrderView> {
    @Override
    public PurchaseOrderView convert(@NonNull PurchaseOrderDomain source) {
        return PurchaseOrderView.builder()
                .id(source.getId())
                .idUser(source.getIdUser())
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
