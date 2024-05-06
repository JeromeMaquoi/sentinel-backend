package com.snail.sentinel.backend.commons;

public class TestingLoggingToFile implements LoggingToFile {
    @Override
    public void writeTimeToFileForWarningIterationResult(int numberOfCell, String message) { /* no logs written into files for tests */ }

    @Override
    public void writeTimeToFile(String lineToAdd) { /* no logs written into files for tests */ }
}
