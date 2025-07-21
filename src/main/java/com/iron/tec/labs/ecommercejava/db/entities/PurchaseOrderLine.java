package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "PURCHASE_ORDER_LINE")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class PurchaseOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_purchase_order", nullable = false, foreignKey = @ForeignKey(name="po_id_purchase_order_fk"))
    @NotNull
    private PurchaseOrder purchaseOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false, foreignKey = @ForeignKey(name="po_id_product_fk"))
    @NotNull
    private Product product;
    
    @NotNull
    private Integer quantity;
}
