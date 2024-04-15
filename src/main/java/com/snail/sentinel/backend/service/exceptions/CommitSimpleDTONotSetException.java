package com.snail.sentinel.backend.service.exceptions;

public class CommitSimpleDTONotSetException extends IllegalStateException {
    public CommitSimpleDTONotSetException(String methodName) {
        super("commitSimpleDTO should be set before using " + methodName + " method");
    }
}
