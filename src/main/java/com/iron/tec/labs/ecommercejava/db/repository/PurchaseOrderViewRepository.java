package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrderView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseOrderViewRepository extends JpaRepository<PurchaseOrderView, UUID> {

}
