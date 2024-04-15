package com.snail.sentinel.backend.service.exceptions;

public class FileListProviderNotSetException extends IllegalStateException {
    public FileListProviderNotSetException(String methodName) {
        super("fileListProvider should be set before using " + methodName + " method");
    }
}
