package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class PurchaseOrderDTO {
    private String id;
    @Singular
    private Set<PurchaseOrderLineDTO> lines;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private UserDTO user;
}