package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetPurchaseOrderLineViewMapper implements Converter<PurchaseOrderLineView, PurchaseOrderLineDTO> {

    @Override
    public PurchaseOrderLineDTO convert(@NonNull PurchaseOrderLineView source) {
        return PurchaseOrderLineDTO.builder()
                .idProduct(source.getIdProduct().toString())
                .quantity(source.getQuantity())
                .price(source.getPrice())
                .productName(source.getProductName())
                .smallImageUrl(source.getSmallImageUrl())
                .build();
    }
}