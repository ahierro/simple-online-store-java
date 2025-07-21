package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLineView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseOrderLineViewRepository extends JpaRepository<PurchaseOrderLineView, UUID> {
    List<PurchaseOrderLineView> getPurchaseOrderLineByIdPurchaseOrder(UUID purchaseOrderId);
}
