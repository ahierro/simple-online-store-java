package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderViewToDomain implements Converter<@NonNull PurchaseOrderView,@NonNull PurchaseOrderDomain> {
    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrderView source) {
        return PurchaseOrderDomain.builder()
                .id(source.getId())
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .user(AppUserDomain.builder()
                        .id(source.getIdUser())
                        .username(source.getUsername())
                        .email(source.getEmail())
                        .firstName(source.getFirstName())
                        .lastName(source.getLastName())
                        .build())
                .build();
    }
}
