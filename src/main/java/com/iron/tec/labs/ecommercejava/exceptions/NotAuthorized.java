package com.iron.tec.labs.ecommercejava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class NotAuthorized extends RuntimeException {
    public NotAuthorized() {
    }

    public NotAuthorized(String message) {
        super(message);
    }
}
