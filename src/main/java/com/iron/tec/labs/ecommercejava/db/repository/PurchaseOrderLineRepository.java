package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;
@Repository
public interface PurchaseOrderLineRepository extends R2dbcRepository<PurchaseOrderLine, UUID> {
    Flux<PurchaseOrderLine> getPurchaseOrderLineByIdPurchaseOrder(UUID purchaseOrderId);
}
