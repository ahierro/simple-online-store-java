package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {
    @NotEmpty
    private @Length(max = 50) String name;
    @NotEmpty
    private String description;
}