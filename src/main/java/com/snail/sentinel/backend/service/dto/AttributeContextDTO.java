package com.snail.sentinel.backend.service.dto;

import java.util.Objects;

public class AttributeContextDTO {
    private String name;
    private String type;
    private String actualType;
    private String rhs;

    public AttributeContextDTO() {}

    public AttributeContextDTO(String name, String type, String actualType, String rhs) {
        this.name = name;
        this.type = type;
        this.actualType = actualType;
        this.rhs = rhs;
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

    public String getRhs() {
        return rhs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttributeContextDTO that = (AttributeContextDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(actualType, that.actualType) && Objects.equals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, actualType, rhs);
    }

    @Override
    public String toString() {
        return "AttributeContextDTO{" +
            "name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", actualType='" + actualType + '\'' +
            ", rhs='" + rhs + '\'' +
            '}';
    }
}
