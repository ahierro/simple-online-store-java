package com.iron.tec.labs.ecommercejava.db.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("PRODUCT_VIEW")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class ProductView {
    @Id
    private UUID id;
    private String productName;
    private String productDescription;
    private Integer stock;
    @EqualsAndHashCode.Exclude
    private BigDecimal price;
    private String smallImageUrl;
    private String bigImageUrl;
    @EqualsAndHashCode.Include
    private BigDecimal priceWithoutTrailingZeros() {
        return price != null ? price.stripTrailingZeros() : null;
    }
    private UUID idCategory;
    private String categoryName;
    private String categoryDescription;
    @EqualsAndHashCode.Exclude
    private LocalDateTime productCreatedAt;
    @EqualsAndHashCode.Exclude
    private LocalDateTime categoryCreatedAt;
    private Boolean deleted;

}
