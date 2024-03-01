package com.snail.sentinel.backend.service.exceptions;

public class NoCsvFileFoundException extends NullPointerException {
    public NoCsvFileFoundException(NullPointerException e) {
        super("No CSV file found in this file " + e);
    }
}
