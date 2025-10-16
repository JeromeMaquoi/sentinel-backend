package com.snail.sentinel.backend.service.exceptions;

public class ConstructorContextEntityExistsException extends RuntimeException {
    public ConstructorContextEntityExistsException(String message) {
        super(message);
    }
}
