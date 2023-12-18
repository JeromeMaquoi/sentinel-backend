package com.snail.sentinel.backend.service.exceptions;

public class CsvFileNotFoundException extends RuntimeException {
    public CsvFileNotFoundException() {
        super("Csv file not found!");
    }
}
