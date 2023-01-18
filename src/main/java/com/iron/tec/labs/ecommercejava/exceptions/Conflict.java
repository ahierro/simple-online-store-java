package com.iron.tec.labs.ecommercejava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class Conflict extends RuntimeException {
    public Conflict() {
    }

    public Conflict(String message) {
        super(message);
    }
}
