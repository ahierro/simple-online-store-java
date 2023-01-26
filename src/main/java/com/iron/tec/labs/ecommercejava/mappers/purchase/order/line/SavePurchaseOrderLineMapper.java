package com.iron.tec.labs.ecommercejava.mappers.purchase.order.line;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderLineDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface SavePurchaseOrderLineMapper extends Converter<PurchaseOrderLineDTO, PurchaseOrderLine> {
    PurchaseOrderLine convert(@NonNull PurchaseOrderLineDTO product);
}