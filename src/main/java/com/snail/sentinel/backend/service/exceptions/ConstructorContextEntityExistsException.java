package com.snail.sentinel.backend.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConstructorContextEntityExistsException extends RuntimeException {
    public ConstructorContextEntityExistsException(String message) {
        super(message);
    }
}
