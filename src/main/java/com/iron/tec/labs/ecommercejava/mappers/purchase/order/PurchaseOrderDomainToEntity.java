package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseOrderDomainToEntity implements Converter<PurchaseOrderDomain, PurchaseOrder> {
    private final ConversionService conversionService;

    public PurchaseOrderDomainToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PurchaseOrder convert(@NonNull PurchaseOrderDomain source) {
        List<PurchaseOrderLine> lines = null;
        if (source.getLines() != null) {
            lines = new ArrayList<>();
            for (PurchaseOrderLineDomain domain : source.getLines()) {
                PurchaseOrderLine line = conversionService.convert(domain, PurchaseOrderLine.class);
                if (line != null) {
                    lines.add(line);
                }
            }
        }
        return PurchaseOrder.builder()
                .id(source.getId())
                .idUser(source.getIdUser())
                .lines(lines)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
