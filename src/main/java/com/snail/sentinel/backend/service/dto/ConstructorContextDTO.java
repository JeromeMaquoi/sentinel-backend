package com.snail.sentinel.backend.service.dto;


import java.util.List;
import java.util.Objects;

public class ConstructorContextDTO {
    private String fileName;
    private String className;
    private String methodName;
    private List<String> parameters;
    private List<AttributeContextDTO> attributes;
    private List<StackTraceElement> stacktrace;
    private String snapshot;

    public ConstructorContextDTO() {}

    public ConstructorContextDTO(String fileName, String className, String methodName, List<String> parameters, List<AttributeContextDTO> attributes, List<StackTraceElement> stacktrace, String snapshot) {
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.attributes = attributes;
        this.stacktrace = stacktrace;
        this.snapshot = snapshot;
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

    public List<String> getParameters() {
        return parameters;
    }

    public List<AttributeContextDTO> getAttributes() {
        return attributes;
    }

    public List<StackTraceElement> getStacktrace() {
        return stacktrace;
    }

    public String getSnapshot() {
        return snapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorContextDTO that = (ConstructorContextDTO) o;
        return Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters) && Objects.equals(attributes, that.attributes) && Objects.equals(stacktrace, that.stacktrace) && Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, className, methodName, parameters, attributes, stacktrace, snapshot);
    }
}
