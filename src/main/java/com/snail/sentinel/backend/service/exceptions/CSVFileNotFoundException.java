package com.snail.sentinel.backend.service.exceptions;

import java.io.FileNotFoundException;

public class CSVFileNotFoundException extends FileNotFoundException {
    public CSVFileNotFoundException(String csvFilePath, Exception exception) {
        super("CSV file not found: " + csvFilePath + "\n" + exception.getMessage());
    }
}
