package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseOrderEntityToDomain implements Converter<PurchaseOrder, PurchaseOrderDomain> {
    private final ConversionService conversionService;

    public PurchaseOrderEntityToDomain(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrder source) {
        List<PurchaseOrderLineDomain> lines = new ArrayList<>();
        if (source.getLines() != null) {
            for (PurchaseOrderLine line : source.getLines()) {
                PurchaseOrderLineDomain domain = conversionService.convert(line, PurchaseOrderLineDomain.class);
                if (domain != null) {
                    lines.add(domain);
                }
            }
        } else if (source.getViewLines() != null) {
            for (PurchaseOrderLineView view : source.getViewLines()) {
                PurchaseOrderLineDomain domain = conversionService.convert(view, PurchaseOrderLineDomain.class);
                if (domain != null) {
                    lines.add(domain);
                }
            }
        }
        AppUserDomain user = null;
        if (source.getUser() != null) {
            user = conversionService.convert(source.getUser(), AppUserDomain.class);
        }
        return PurchaseOrderDomain.builder()
                .id(source.getId())
                .idUser(source.getIdUser())
                .lines(lines)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .user(user)
                .build();
    }
}
