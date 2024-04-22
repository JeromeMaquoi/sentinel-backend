package com.snail.sentinel.backend.service.dto.joularnode;

import java.util.List;
import java.util.Objects;

public class JoularNodeKeyHashMap {
    private String classMethodSignature;
    private int lineNumber;
    private List<String> ancestors;

    public JoularNodeKeyHashMap(String classMethodSignature, int lineNumber, List<String> ancestors) {
        this.classMethodSignature = classMethodSignature;
        this.lineNumber = lineNumber;
        this.ancestors = ancestors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularNodeKeyHashMap that = (JoularNodeKeyHashMap) o;
        return lineNumber == that.lineNumber && Objects.equals(classMethodSignature, that.classMethodSignature) && Objects.equals(ancestors, that.ancestors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classMethodSignature, lineNumber, ancestors);
    }

    @Override
    public String toString() {
        return "JoularNodeKeyHashMap{" +
            "classMethodSignature='" + classMethodSignature + '\'' +
            ", lineNumber=" + lineNumber +
            ", ancestors=" + ancestors +
            '}';
    }
}
