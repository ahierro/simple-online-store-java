package com.iron.tec.labs.ecommercejava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateKey extends RuntimeException {
    public DuplicateKey() {
    }

    public DuplicateKey(String message) {
        super(message);
    }
}
