package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseOrderDAO {
    Mono<PurchaseOrder> getById(UUID id);
    Mono<Page<PurchaseOrderView>> getPage(int page, int size, PurchaseOrderView purchaseOrderExample);
    Mono<PurchaseOrder> create(PurchaseOrder product);
    Mono<PurchaseOrder> update(PurchaseOrder product);
}
