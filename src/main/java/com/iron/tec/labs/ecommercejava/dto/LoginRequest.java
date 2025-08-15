package com.iron.tec.labs.ecommercejava.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @Schema(description = "Username of the user", example = "admin")
        @NotEmpty @Length(min = 4, max = 50) String username,
        @Schema(description = "Password of the user", example = "p4ssw0rd")
        @NotEmpty @Length(min = 5, max = 50) String password) {

}
