package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderCreationDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface SavePurchaseOrderMapper extends Converter<PurchaseOrderCreationDTO, PurchaseOrder>  {
    PurchaseOrder convert(@NonNull PurchaseOrderCreationDTO product);
}