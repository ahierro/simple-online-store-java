package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.AppUser;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineDomainToEntity;
import org.springframework.core.convert.converter.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseOrderDomainToEntity implements Converter<@NonNull PurchaseOrderDomain,@NonNull PurchaseOrder> {
    private final PurchaseOrderLineDomainToEntity conversionService;

    public PurchaseOrderDomainToEntity(PurchaseOrderLineDomainToEntity conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public PurchaseOrder convert(@NonNull PurchaseOrderDomain source) {
        List<PurchaseOrderLine> lines = null;
        if (source.getLines() != null) {
            lines = new ArrayList<>();
            for (PurchaseOrderLineDomain domain : source.getLines()) {
                PurchaseOrderLine line = conversionService.convert(domain);
                if (line != null) {
                    lines.add(line);
                }
            }
        }
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .id(source.getId())
                .user(conversionService.getEntityManager().getReference(AppUser.class, source.getUser().getId()))
                .lines(lines)
                .total(source.getTotal())
                .status(source.getStatus())
                .createdAt(source.getCreatedAt())
                .build();
        purchaseOrder.getLines()
                .forEach(line -> line.setPurchaseOrder(purchaseOrder));
        return purchaseOrder;
    }
}
