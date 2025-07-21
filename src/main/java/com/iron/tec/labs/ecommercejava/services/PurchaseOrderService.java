package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;
import com.iron.tec.labs.ecommercejava.dto.PageRequestDTO;

import java.util.UUID;

public interface PurchaseOrderService {
    PurchaseOrderDomain getById(UUID id);

    PurchaseOrderDomain createPurchaseOrder(PurchaseOrderDomain purchaseOrder);

    PurchaseOrderDomain patchPurchaseOrder(String id, PurchaseOrderDomain purchaseOrder);

    PageDomain<PurchaseOrderDomain> getPurchaseOrderPage(PageRequestDTO pageRequest);

}
