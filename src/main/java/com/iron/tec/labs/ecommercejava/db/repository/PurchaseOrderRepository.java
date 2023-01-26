package com.iron.tec.labs.ecommercejava.db.repository;

import com.iron.tec.labs.ecommercejava.db.entities.PurchaseOrder;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Repository
public interface PurchaseOrderRepository extends R2dbcRepository<PurchaseOrder, UUID> {

    @Modifying
    @Query("delete from purchase_order where id = :id")
    Mono<Integer> deletePurchaseOrderById(UUID id);
}
