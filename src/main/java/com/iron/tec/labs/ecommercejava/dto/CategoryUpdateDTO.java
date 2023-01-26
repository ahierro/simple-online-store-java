package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryUpdateDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private Boolean deleted;
}