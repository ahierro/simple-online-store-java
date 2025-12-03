package com.iron.tec.labs.ecommercejava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderViewDTO {
    private String id;
    private String idUser;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}