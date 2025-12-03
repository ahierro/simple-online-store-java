package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("purchase_order_line")
@Data
@SuperBuilder
@NoArgsConstructor
public class PurchaseOrderLine implements Persistable<UUID> {

    @Id
    private UUID id;
    @NotNull
    private UUID idPurchaseOrder;
    @NotNull
    private UUID idProduct;
    @NotNull
    private Integer quantity;

    @Transient
    private ProductView product;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
