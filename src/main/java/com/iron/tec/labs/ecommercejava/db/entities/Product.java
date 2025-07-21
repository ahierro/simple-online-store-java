package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "PRODUCT")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends AuditableEntity {

    private String name;
    private String description;
    private Integer stock;
    
    @Positive
    @EqualsAndHashCode.Exclude
    private BigDecimal price;
    
    private String smallImageUrl;
    private String bigImageUrl;
    private Boolean deleted;

    @EqualsAndHashCode.Include
    private BigDecimal priceWithoutTrailingZeros() {
        return price != null ? price.stripTrailingZeros() : null;
    }
    
    @Column(name = "id_category")
    private UUID idCategory;
}
