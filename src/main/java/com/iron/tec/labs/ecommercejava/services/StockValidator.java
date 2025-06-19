package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import reactor.core.publisher.Mono;

public interface StockValidator {
    Mono<PurchaseOrderDomain> validateStock(PurchaseOrderDomain purchaseOrder);
}
