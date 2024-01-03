package com.snail.sentinel.backend.service.dto.measurableelement;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;

public class MeasurableElementDTO {
    @NotNull
    private String astElem;

    @NotNull
    private String filePath;

    @NotNull
    @Indexed
    private String className;

    private String methodSignature;

    private String variableName;

    private String classType;

    public String getAstElem() {
        return astElem;
    }

    public  void setAstElem(String astElem) {
        this.astElem = astElem;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurableElementDTO that = (MeasurableElementDTO) o;
        return Objects.equals(astElem, that.astElem) && Objects.equals(filePath, that.filePath) && Objects.equals(className, that.className) && Objects.equals(methodSignature, that.methodSignature) && Objects.equals(variableName, that.variableName) && Objects.equals(classType, that.classType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(astElem, filePath, className, methodSignature, variableName, classType);
    }

    @Override
    public String toString() {
        return "MeasurableElementDTO{" +
            "astElem='" + astElem + '\'' +
            ", filePath='" + filePath + '\'' +
            ", className='" + className + '\'' +
            ", methodSignature='" + methodSignature + '\'' +
            ", variableName='" + variableName + '\'' +
            ", classType='" + classType + '\'' +
            '}';
    }
}
