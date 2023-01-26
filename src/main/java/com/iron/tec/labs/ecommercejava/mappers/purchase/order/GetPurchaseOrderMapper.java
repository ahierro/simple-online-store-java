package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetPurchaseOrderMapper extends Converter<PurchaseOrder, PurchaseOrderDTO> {
    @Mapping(target = "lines", source = "viewLines")
    PurchaseOrderDTO convert(@NonNull PurchaseOrder product);
}