package com.snail.sentinel.backend.service.dto;

import java.util.Objects;

public class AttributeContextDTO {
    private String name;
    private String type;
    private String actualType;
    private String rhsType;

    public AttributeContextDTO() {}

    public AttributeContextDTO(String name, String type, String actualType, String rhsType) {
        this.name = name;
        this.type = type;
        this.actualType = actualType;
        this.rhsType = rhsType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getActualType() {
        return actualType;
    }

    public String getRhsType() {
        return rhsType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttributeContextDTO that = (AttributeContextDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(actualType, that.actualType) && Objects.equals(rhsType, that.rhsType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, actualType, rhsType);
    }
}
