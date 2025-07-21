package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "PURCHASE_ORDER")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrder extends AuditableEntity {

    @NotNull
    @Column(name = "id_user")
    private UUID idUser;

    @Transient
    private AppUser user;

    @Transient
    @Singular
    private List<PurchaseOrderLineView> viewLines;
    
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
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
