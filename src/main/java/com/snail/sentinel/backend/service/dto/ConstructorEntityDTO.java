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

    private String pkg;

    private String file;

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

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
            ", pkg='" + getPkg() + "'" +
            ", file='" + getFile() + "'" +
            "}";
    }
}
