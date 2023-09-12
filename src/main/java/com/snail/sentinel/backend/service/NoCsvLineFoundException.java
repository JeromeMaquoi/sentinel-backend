package com.snail.sentinel.backend.service;

import java.io.IOException;

public class NoCsvLineFoundException extends RuntimeException{
    public NoCsvLineFoundException(IOException e) {
        super("No CSV line found in this file " + e);
    }
}
