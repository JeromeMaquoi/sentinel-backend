package com.snail.sentinel.backend.service.dto.ck;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;


public class CkAggregateLineDTO {
    @Id
    private Object id;
    private String className;
    private String filePath;
    private String methodName;
    private List<Integer> line;
    private List<Integer> loc;

    public String getLabel() {
        return this.className + "." + this.methodName;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getLine() {
        return line.get(0);
    }

    public void setLine(List<Integer> line) {
        this.line = line;
    }

    public Integer getLoc() {
        return loc.get(0);
    }

    public void setLoc(List<Integer> loc) {
        this.loc = loc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CkAggregateLineDTO that = (CkAggregateLineDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(className, that.className) && Objects.equals(filePath, that.filePath) && Objects.equals(methodName, that.methodName) && Objects.equals(line, that.line) && Objects.equals(loc, that.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, className, filePath, methodName, line, loc);
    }

    @Override
    public String toString() {
        return "CkAggregateLineDTO{" +
            "id=" + id +
            ", className='" + className + '\'' +
            ", filePath='" + filePath + '\'' +
            ", methodSignature='" + methodName + '\'' +
            ", line=" + line.get(0) +
            ", loc=" + loc.get(0) +
            '}';
    }
}
