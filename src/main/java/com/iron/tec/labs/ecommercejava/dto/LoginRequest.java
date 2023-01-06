package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(@NotEmpty @Length(min = 6, max = 50) String username,
                           @NotEmpty @Length(min = 6, max = 50) String password) {

}
