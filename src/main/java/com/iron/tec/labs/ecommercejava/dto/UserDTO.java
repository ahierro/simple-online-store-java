package com.iron.tec.labs.ecommercejava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
