package com.snail.sentinel.backend.commons;

public interface LoggingToFile {
    void writeTimeToFileForWarningIterationResult(int numberOfCell, String message);
    void writeTimeToFile(String lineToAdd);
}
