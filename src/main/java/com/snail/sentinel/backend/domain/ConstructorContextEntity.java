package com.snail.sentinel.backend.domain;

import com.snail.sentinel.backend.service.dto.AttributeContextDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Document(collection = "constructor_context_entity")
public class ConstructorContextEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Field("file")
    private String fileName;
    @Field("class")
    private String className;
    @Field("method")
    private String methodName;
    @Field("parameters")
    private List<String> parameters;
    @Field("attributes")
    private List<AttributeContextDTO> attributes;
    @Field("stacktrace")
    private List<StackTraceElementDTO> stacktrace;
    @Field("snapshot")
    private String snapshot;

    public String getId() {
        return id;
    }

    public ConstructorContextEntity withId(String id) {
        this.id = id;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public ConstructorContextEntity withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClassName() {
        return className;
    }

    public ConstructorContextEntity withClassName(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public ConstructorContextEntity withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public ConstructorContextEntity withParameters(List<String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<AttributeContextDTO> getAttributes() {
        return attributes;
    }

    public ConstructorContextEntity withAttributes(List<AttributeContextDTO> attributes) {
        this.attributes = attributes;
        return this;
    }

    public void setAttributes(List<AttributeContextDTO> attributes) {
        this.attributes = attributes;
    }

    public List<StackTraceElementDTO> getStacktrace() {
        return stacktrace;
    }

    public ConstructorContextEntity withStacktrace(List<StackTraceElementDTO> stacktrace) {
        this.stacktrace = stacktrace;
        return this;
    }

    public void setStacktrace(List<StackTraceElementDTO> stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public ConstructorContextEntity withSnapshot(String snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorContextEntity that = (ConstructorContextEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters) && Objects.equals(attributes, that.attributes) && Objects.equals(stacktrace, that.stacktrace) && Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, className, methodName, parameters, attributes, stacktrace, snapshot);
    }

    @Override
    public String toString() {
        return "ConstructorContextEntity{" +
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
