package com.snail.sentinel.backend.service.exceptions;

public class JoularEntityDTONotFoundException extends RuntimeException {
    public JoularEntityDTONotFoundException(Exception e) {
        super("JoularEntityDTO not found! " + e);
    }
}
