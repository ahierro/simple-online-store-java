package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
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