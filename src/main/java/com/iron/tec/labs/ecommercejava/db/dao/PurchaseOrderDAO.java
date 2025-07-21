package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;

import java.util.UUID;

public interface PurchaseOrderDAO {
    PurchaseOrderDomain getById(UUID id);
    PageDomain<PurchaseOrderDomain> getPage(int page, int size, PurchaseOrderDomain purchaseOrderExample);
    PurchaseOrderDomain create(PurchaseOrderDomain product);
    PurchaseOrderDomain update(PurchaseOrderDomain product);
}
