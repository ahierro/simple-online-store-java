package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, UUID> {
    List<PurchaseOrderLine> findByPurchaseOrder_Id(UUID purchaseOrderId);
}
