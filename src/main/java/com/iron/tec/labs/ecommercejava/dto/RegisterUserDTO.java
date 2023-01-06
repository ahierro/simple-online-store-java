package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class RegisterUserDTO {
    @NotEmpty
    @Length(min = 6,max = 50)
    private String username;
    @NotEmpty
    @Length(min = 6,max = 50)
    private String password;
    @NotEmpty
    @Length(min = 6,max = 50)
    private String email;

    @NotEmpty
    @Length(max = 100)
    private String firstName;

    @NotEmpty
    @Length(max = 100)
    private String lastName;
}