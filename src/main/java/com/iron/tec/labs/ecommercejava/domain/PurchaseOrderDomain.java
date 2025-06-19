package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDomain {
    private UUID id;
    private UUID idUser;
    @Singular
    private List<PurchaseOrderLineDomain> lines;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private AppUserDomain user;
}
