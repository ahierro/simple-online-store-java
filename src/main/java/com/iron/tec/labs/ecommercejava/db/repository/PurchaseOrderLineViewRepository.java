package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface PurchaseOrderLineViewRepository extends R2dbcRepository<PurchaseOrderLineView, UUID> {
    Flux<PurchaseOrderLineView> getPurchaseOrderLineByIdPurchaseOrder(UUID purchaseOrderId);
}
