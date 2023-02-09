package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import reactor.core.publisher.Mono;

public interface StockValidator {
    Mono<PurchaseOrder> validateStock(PurchaseOrder purchaseOrder);
}
