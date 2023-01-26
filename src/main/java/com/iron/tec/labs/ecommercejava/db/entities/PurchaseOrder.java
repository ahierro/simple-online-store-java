package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Table("PURCHASE_ORDER")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrder extends AuditableEntity {

    @NotNull
    private UUID idUser;

    @Transient
    private AppUser user;

    @Transient
    @Singular
    private List<PurchaseOrderLineView> viewLines;
    @Transient
    @Singular
    private List<PurchaseOrderLine> lines;

    @Positive
    @EqualsAndHashCode.Exclude
    @NotNull
    private BigDecimal total;

    @NotNull
    private String status;

    @EqualsAndHashCode.Include
    private BigDecimal totalWithoutTrailingZeros() {
        return total != null ? total.stripTrailingZeros() : null;
    }

}
