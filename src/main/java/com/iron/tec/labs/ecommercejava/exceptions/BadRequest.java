package com.iron.tec.labs.ecommercejava.exceptions;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@StandardException
public class BadRequest extends RuntimeException {
}
