package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseOrderDAO {
    Mono<PurchaseOrderDomain> getById(UUID id);
    Mono<PageDomain<PurchaseOrderDomain>> getPage(int page, int size, PurchaseOrderDomain purchaseOrderExample);
    Mono<PurchaseOrderDomain> create(PurchaseOrderDomain product);
    Mono<PurchaseOrderDomain> update(PurchaseOrderDomain product);
}
