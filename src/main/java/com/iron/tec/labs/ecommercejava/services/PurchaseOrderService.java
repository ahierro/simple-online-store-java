package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PurchaseOrderService {
    Mono<PurchaseOrderDomain> getById(UUID id);

    Mono<PurchaseOrderDomain> createPurchaseOrder(PurchaseOrderDomain purchaseOrder);

    Mono<PurchaseOrderDomain> patchPurchaseOrder(String id, PurchaseOrderDomain purchaseOrder);

    Mono<PageDomain<PurchaseOrderDomain>> getPurchaseOrderPage(PageRequestDTO pageRequest);

}
