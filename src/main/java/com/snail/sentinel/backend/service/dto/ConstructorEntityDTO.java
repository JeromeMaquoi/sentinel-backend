package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.ConstructorEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConstructorEntityDTO implements Serializable {

    private String id;

    private String name;

    private String signature;

    private String className;

    private String fileName;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstructorEntityDTO)) {
            return false;
        }

        ConstructorEntityDTO constructorEntityDTO = (ConstructorEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, constructorEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConstructorEntityDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", signature='" + getSignature() + "'" +
            ", className='" + getClassName() + "'" +
            ", file='" + getFileName() + "'" +
            "}";
    }
}
