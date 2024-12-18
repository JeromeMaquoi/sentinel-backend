package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class AttributeEntityDTO implements Serializable {

    private String id;

    private String name;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttributeEntityDTO that = (AttributeEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "AttributeEntityDTO{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            '}';
    }
}
