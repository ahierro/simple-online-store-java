package com.iron.tec.labs.ecommercejava.db.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false, foreignKey = @ForeignKey(name = "p_category_fk"))
    @EqualsAndHashCode.Exclude
    private Category category;

    @Positive
    @EqualsAndHashCode.Exclude
    private BigDecimal price;

    private String smallImageUrl;
    private String bigImageUrl;

    @EqualsAndHashCode.Include
    private BigDecimal priceWithoutTrailingZeros() {
        return price != null ? price.stripTrailingZeros() : null;
    }
}
