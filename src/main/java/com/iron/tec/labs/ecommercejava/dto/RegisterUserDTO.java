package com.iron.tec.labs.ecommercejava.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterUserDTO {
    @NotEmpty
    @Length(min = 6,max = 50)
    private String username;
    @NotEmpty
    @Length(min = 6,max = 50)
    private String password;
    @NotEmpty
    @Length(min = 6,max = 50)
    @Email
    private String email;

    @NotEmpty
    @Length(max = 100)
    private String firstName;

    @NotEmpty
    @Length(max = 100)
    private String lastName;

    // This is just for testing purposes
    private Boolean isAdmin;
}