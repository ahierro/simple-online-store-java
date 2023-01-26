package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
@Mapper(componentModel = "spring")
public interface UpdatePurchaseOrderMapper extends Converter<PurchaseOrderDTO, PurchaseOrder>  {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    PurchaseOrder convert(@NonNull PurchaseOrderDTO product);

}