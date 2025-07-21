package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;

public interface StockValidator {
    PurchaseOrderDomain validateStock(PurchaseOrderDomain purchaseOrder);
}
