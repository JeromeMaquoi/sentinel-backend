package com.snail.sentinel.backend.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Document(collection="ast_elem_entity")
public class AstElemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Field("astElem")
    private String astElem;
    @Field("file")
    private String fileName;
    @Field("class")
    private String className;
    @Field("method")
    private String methodName;
    @Field("parameters")
    private List<String> parameters;
    @Field("line")
    private int line;
    @Field("loc")
    private int loc;

    public String getId() {
        return id;
    }

    public AstElemEntity withId(String id) {
        this.id = id;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAstElem() {
        return astElem;
    }

    public AstElemEntity withAstElem(String astElem) {
        this.astElem = astElem;
        return this;
    }

    public void setAstElem(String astElem) {
        this.astElem = astElem;
    }

    public String getFileName() {
        return fileName;
    }

    public AstElemEntity withFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getClassName() {
        return className;
    }

    public AstElemEntity withClassName(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public AstElemEntity withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public AstElemEntity withParameters(List<String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public int getLine() {
        return line;
    }

    public AstElemEntity withLine(int line) {
        this.line = line;
        return this;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLoc() {
        return loc;
    }

    public AstElemEntity withLoc(int loc) {
        this.loc = loc;
        return this;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AstElemEntity that = (AstElemEntity) o;
        return line == that.line && Objects.equals(id, that.id) && Objects.equals(astElem, that.astElem) && Objects.equals(fileName, that.fileName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Objects.equals(parameters, that.parameters) && Objects.equals(loc, that.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, astElem, fileName, className, methodName, parameters, line, loc);
    }

    @Override
    public String toString() {
        return "AstElemEntity{" +
            "id='" + id + '\'' +
            ", astElem='" + astElem + '\'' +
            ", fileName='" + fileName + '\'' +
            ", className='" + className + '\'' +
            ", methodName='" + methodName + '\'' +
            ", parameters=" + parameters +
            ", line=" + line +
            ", loc='" + loc + '\'' +
            '}';
    }
}
