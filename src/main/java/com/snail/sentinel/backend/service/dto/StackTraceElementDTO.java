package com.snail.sentinel.backend.service.dto;

import java.util.List;
import java.util.Objects;

public class StackTraceElementDTO {
    private String fileName;
    private String className;
    private String methodName;
    private String lineNumber;
    private List<String> parameters;

    public StackTraceElementDTO(String fileName, String className, String methodName, String lineNumber, List<String> parameters) {
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.lineNumber = lineNumber;
        this.parameters = parameters;
    }

    public String getFileName() {
        return fileName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StackTraceElementDTO that = (StackTraceElementDTO) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(lineNumber, that.lineNumber) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, className, methodName, lineNumber, parameters);
    }

    @Override
    public String toString() {
        return "StackTraceElementDTO{" +
            "fileName='" + fileName + '\'' +
            ", className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            ", lineNumber='" + lineNumber + '\'' +
            ", parameters=" + parameters +
            '}';
    }
}
