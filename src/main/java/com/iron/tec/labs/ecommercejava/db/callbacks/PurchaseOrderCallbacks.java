package com.iron.tec.labs.ecommercejava.db.callbacks;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.repository.PurchaseOrderLineRepository;

import reactor.core.publisher.Mono;

@Component
public class PurchaseOrderCallbacks implements AfterSaveCallback<PurchaseOrder> {

    private final PurchaseOrderLineRepository purchaseOrderLineRepository;

    public PurchaseOrderCallbacks(@Lazy PurchaseOrderLineRepository purchaseOrderLineRepository) {
        this.purchaseOrderLineRepository = purchaseOrderLineRepository;
    }

    @Override
    public @NonNull Publisher<PurchaseOrder> onAfterSave(@NonNull PurchaseOrder entity,@NonNull OutboundRow outboundRow,@NonNull SqlIdentifier table) {
        if(entity.getLines() == null || entity.getLines().isEmpty())
            return Mono.just(entity);
        entity.getLines().forEach(line -> line.setIdPurchaseOrder(entity.getId()));
        return purchaseOrderLineRepository.saveAll(entity.getLines()).collectList().map(line -> entity);
    }

}
