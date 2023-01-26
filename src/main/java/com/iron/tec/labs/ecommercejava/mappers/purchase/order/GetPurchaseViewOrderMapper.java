package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import com.iron.tec.labs.ecommercejava.dto.PurchaseOrderViewDTO;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface GetPurchaseViewOrderMapper extends Converter<PurchaseOrderView, PurchaseOrderViewDTO> {

    PurchaseOrderViewDTO convert(@NonNull PurchaseOrderView product);
}