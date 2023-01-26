package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetPurchaseOrderLineViewMapper extends Converter<PurchaseOrderLineView, PurchaseOrderLineDTO> {
    PurchaseOrderLineDTO convert(@NonNull PurchaseOrderLineView product);
}