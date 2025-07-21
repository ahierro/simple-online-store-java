package com.iron.tec.labs.ecommercejava.db.dao;

import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import com.iron.tec.labs.ecommercejava.domain.PurchaseOrderDomain;

import java.util.UUID;

public interface PurchaseOrderDAOTransactionHandler {
    PurchaseOrderDomain create(PurchaseOrderDomain product);
    PurchaseOrderDomain update(PurchaseOrderDomain product);
}
