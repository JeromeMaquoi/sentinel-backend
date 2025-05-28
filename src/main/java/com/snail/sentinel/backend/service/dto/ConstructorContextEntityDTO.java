package com.snail.sentinel.backend.service.dto;

import java.util.List;
import java.util.Objects;

public class ConstructorContextEntityDTO {
    private String id;
    private String fileName;
    private String className;
    private String methodName;
    private List<String> parameters;
    private List<AttributeContextDTO> attributes;
    private List<StackTraceElementDTO> stacktrace;
    private String snapshot;

    public ConstructorContextEntityDTO(String id, String fileName, String className, String methodName, List<String> parameters, List<AttributeContextDTO> attributes, List<StackTraceElementDTO> stacktrace, String snapshot) {
        this.id = id;
        this.fileName = fileName;
        this.className = className;
        this.methodName = methodName;
        this.parameters = parameters;
        this.attributes = attributes;
        this.stacktrace = stacktrace;
        this.snapshot = snapshot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<AttributeContextDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeContextDTO> attributes) {
        this.attributes = attributes;
    }

    public List<StackTraceElementDTO> getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(List<StackTraceElementDTO> stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorContextEntityDTO that = (ConstructorContextEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters) && Objects.equals(attributes, that.attributes) && Objects.equals(stacktrace, that.stacktrace) && Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, className, methodName, parameters, attributes, stacktrace, snapshot);
    }

    @Override
    public String toString() {
        return "ConstructorContextEntityDTO{" +
            "id='" + id + '\'' +
            ", fileName='" + fileName + '\'' +
            ", className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            ", parameters=" + parameters +
            ", attributes=" + attributes +
            ", stacktrace=" + stacktrace +
            ", snapshot='" + snapshot + '\'' +
            '}';
    }
}
