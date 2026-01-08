package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreationDTO {
    @NotEmpty @UUID private String id;
    @NotEmpty @Length(max = 50) private String name;
    @NotEmpty private String description;
}