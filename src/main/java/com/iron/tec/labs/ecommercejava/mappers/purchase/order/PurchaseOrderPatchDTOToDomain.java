package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderPatchDTO;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderPatchDTOToDomain implements Converter<@NonNull PurchaseOrderPatchDTO,@NonNull PurchaseOrderDomain> {
    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrderPatchDTO source) {
        return PurchaseOrderDomain.builder()
                .status(source.getStatus().name())
                .build();
    }
}
