package com.iron.tec.labs.ecommercejava.domain;

import lombok.*;
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
    private UUID idCategory;
    private Boolean deleted;
    private LocalDateTime createdAt;
}
