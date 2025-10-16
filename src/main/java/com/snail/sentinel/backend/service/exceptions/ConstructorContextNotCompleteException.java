package com.snail.sentinel.backend.service.exceptions;

public class ConstructorContextNotCompleteException extends RuntimeException {
    public ConstructorContextNotCompleteException() {
        super("ConstructorContext not completely set.");
    }
}
