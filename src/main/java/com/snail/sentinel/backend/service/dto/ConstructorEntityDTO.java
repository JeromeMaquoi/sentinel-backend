package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.ConstructorEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConstructorEntityDTO implements Serializable {

    private String id;

    private String signature;

    private String className;

    private String fileName;

    private Set<AttributeEntityDTO> attributeEntities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Set<AttributeEntityDTO> getAttributeEntities() {
        return attributeEntities;
    }

    public void setAttributeEntities(Set<AttributeEntityDTO> attributeEntities) {
        this.attributeEntities = attributeEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorEntityDTO that = (ConstructorEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(signature, that.signature) && Objects.equals(className, that.className) && Objects.equals(fileName, that.fileName) && Objects.equals(attributeEntities, that.attributeEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, signature, className, fileName, attributeEntities);
    }

    @Override
    public String toString() {
        return "ConstructorEntityDTO{" +
            "id='" + id + '\'' +
            ", signature='" + signature + '\'' +
            ", className='" + className + '\'' +
            ", fileName='" + fileName + '\'' +
            ", attributeEntities=" + attributeEntities +
            '}';
    }
}
