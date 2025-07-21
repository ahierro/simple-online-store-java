package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "PURCHASE_ORDER_VIEW")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderView extends AuditableEntity {

    @NotNull
    private UUID idUser;
    
    @Positive
    @EqualsAndHashCode.Exclude
    @NotNull
    private BigDecimal total;
    
    @NotNull
    private String status;
    
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    
    @EqualsAndHashCode.Include
    private BigDecimal totalWithoutTrailingZeros() {
        return total != null ? total.stripTrailingZeros() : null;
    }

}
