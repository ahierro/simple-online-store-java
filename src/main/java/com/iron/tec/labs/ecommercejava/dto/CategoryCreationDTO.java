package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
public class CategoryCreationDTO {
    @NotEmpty @UUID private String id;
    @NotEmpty private String name;
    @NotEmpty private String description;
    private Boolean deleted;
}