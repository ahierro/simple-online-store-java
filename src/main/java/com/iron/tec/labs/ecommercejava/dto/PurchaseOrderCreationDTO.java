package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class PurchaseOrderCreationDTO {
    @NotNull
    @UUID
    private String id;

    @Singular
    @NotEmpty
    private Set<PurchaseOrderLineCreationDTO> lines;

    @NotNull
    @Positive
    private BigDecimal total;
}