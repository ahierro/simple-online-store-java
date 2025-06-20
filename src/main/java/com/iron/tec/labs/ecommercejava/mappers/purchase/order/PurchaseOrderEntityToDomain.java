package com.iron.tec.labs.ecommercejava.mappers.purchase.order;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import com.iron.tec.labs.ecommercejava.domain.AppUserDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderLineDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineEntityToDomain;
import com.iron.tec.labs.ecommercejava.mappers.purchase.order.line.PurchaseOrderLineViewToDomain;
import com.iron.tec.labs.ecommercejava.mappers.user.AppUserEntityToDomain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseOrderEntityToDomain implements Converter<PurchaseOrder, PurchaseOrderDomain> {

    private final PurchaseOrderLineEntityToDomain purchaseOrderLineEntityToDomain;
    private final PurchaseOrderLineViewToDomain purchaseOrderLineViewToDomain;
    private final AppUserEntityToDomain appUserEntityToDomain;

    public PurchaseOrderEntityToDomain(PurchaseOrderLineEntityToDomain purchaseOrderLineEntityToDomain, PurchaseOrderLineViewToDomain purchaseOrderLineViewToDomain, AppUserEntityToDomain appUserEntityToDomain) {
        this.purchaseOrderLineEntityToDomain = purchaseOrderLineEntityToDomain;
        this.purchaseOrderLineViewToDomain = purchaseOrderLineViewToDomain;
        this.appUserEntityToDomain = appUserEntityToDomain;
    }

    @Override
    public PurchaseOrderDomain convert(@NonNull PurchaseOrder source) {
        List<PurchaseOrderLineDomain> lines = new ArrayList<>();
        if (source.getLines() != null) {
            for (PurchaseOrderLine line : source.getLines()) {
                PurchaseOrderLineDomain domain = purchaseOrderLineEntityToDomain.convert(line);
                if (domain != null) {
                    lines.add(domain);
                }
            }
        } else if (source.getViewLines() != null) {
            for (PurchaseOrderLineView view : source.getViewLines()) {
                PurchaseOrderLineDomain domain = purchaseOrderLineViewToDomain.convert(view);
                if (domain != null) {
                    lines.add(domain);
                }
            }
        }
        AppUserDomain user = null;
        if (source.getUser() != null) {
            user = appUserEntityToDomain.convert(source.getUser());
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
