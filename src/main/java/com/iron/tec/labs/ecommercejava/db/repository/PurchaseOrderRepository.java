package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    @Modifying
    @Query("delete from PurchaseOrder where id = :id")
    int deletePurchaseOrderById(UUID id);
}
