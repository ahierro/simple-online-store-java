package com.iron.tec.labs.ecommercejava.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDomain {
    private UUID id;
    private String name;
    private String description;
    private Boolean deleted;
    private LocalDateTime createdAt;
}
