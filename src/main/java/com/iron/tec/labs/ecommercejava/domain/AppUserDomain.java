package com.iron.tec.labs.ecommercejava.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDomain {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
