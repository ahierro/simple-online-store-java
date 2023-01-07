package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageRequestDTO {
    @NotNull
    @Min(0)
    private Integer page;
    @NotNull
    @Min(1)
    private Integer size;
}
