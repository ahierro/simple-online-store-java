package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class PageRequestDTO {
    @NotNull
    @Min(0)
    protected Integer page;
    @NotNull
    @Min(1)
    protected Integer size;
}
