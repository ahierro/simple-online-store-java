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

@Table("PURCHASE_ORDER_LINE_VIEW")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class PurchaseOrderLineView {
    @Id
    private UUID id;
    private UUID idPurchaseOrder;
    private Integer quantity;
    private UUID idProduct;
    private String productName;
    @EqualsAndHashCode.Exclude
    private BigDecimal price;
    private Integer stock;
    private String productDescription;
    private String bigImageUrl;
    private String smallImageUrl;
    private UUID idCategory;
    private String categoryName;
    private String categoryDescription;
    @EqualsAndHashCode.Exclude
    private LocalDateTime productCreatedAt;
    @EqualsAndHashCode.Exclude
    private LocalDateTime categoryCreatedAt;
    private Boolean deleted;

    @EqualsAndHashCode.Include
    private BigDecimal priceWithoutTrailingZeros() {
        return price != null ? price.stripTrailingZeros() : null;
    }
}
