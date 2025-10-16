package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AstElemEntityDTO implements Serializable {
    private String id;
    private String astElem;
    private String fileName;
    private String className;
    private String methodName;
    private List<String> parameters;
    private int line;
    private int loc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAstElem() {
        return astElem;
    }

    public void setAstElem(String astElem) {
        this.astElem = astElem;
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AstElemEntityDTO that = (AstElemEntityDTO) o;
        return line == that.line && loc == that.loc && Objects.equals(id, that.id) && Objects.equals(astElem, that.astElem) && Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, astElem, fileName, className, methodName, parameters, line, loc);
    }

    @Override
    public String toString() {
        return "AstElemEntityDTO{" +
            "id='" + id + '\'' +
            ", astElem='" + astElem + '\'' +
            ", fileName='" + fileName + '\'' +
            ", className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            ", parameters=" + parameters +
            ", line=" + line +
            ", loc=" + loc +
            '}';
    }
}
