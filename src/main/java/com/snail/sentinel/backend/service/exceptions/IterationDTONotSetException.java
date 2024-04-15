package com.snail.sentinel.backend.service.exceptions;

public class IterationDTONotSetException extends IllegalStateException {
    public IterationDTONotSetException(String methodName) {
        super("iterationDTO should be set before using " + methodName + " method");
    }
}
