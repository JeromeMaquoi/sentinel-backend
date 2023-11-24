package com.snail.sentinel.backend.service.dto.measurableelement;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class MethodElementDTO implements MeasurableElementDTO, Serializable {
    @NotNull
    private String astElem;

    @NotNull
    private String filePath;

    @NotNull
    private String className;

    @NotNull
    private String methodSignature;

    @Override
    public String getAstElem() {
        return astElem;
    }

    @Override
    public void setAstElem(String astElem) {
        this.astElem = astElem;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodElementDTO that = (MethodElementDTO) o;
        return Objects.equals(astElem, that.astElem) && Objects.equals(filePath, that.filePath) && Objects.equals(className, that.className) && Objects.equals(methodSignature, that.methodSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(astElem, filePath, className, methodSignature);
    }

    @Override
    public String toString() {
        return "MethodElementDTO{" +
            "\n        astElem='" + astElem + '\'' +
            ",\n        filePath='" + filePath + '\'' +
            ",\n        className='" + className + '\'' +
            ",\n        methodSignature='" + methodSignature + '\'' +
            "\n    }\n";
    }
}
