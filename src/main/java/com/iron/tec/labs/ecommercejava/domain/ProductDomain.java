package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDomain {
    private UUID id;
    private String name;
    private String description;
    private Integer stock;
    private BigDecimal price;
    private String smallImageUrl;
    private String bigImageUrl;
    private CategoryDomain category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
