package com.snail.sentinel.backend.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ConstructorEntity.
 */
@Document(collection = "constructor_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConstructorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("signature")
    private String signature;

    @Field("class")
    private String className;

    @Field("file")
    private String file;

    @Field("attributes")
    private Set<AttributeEntity> attributeEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ConstructorEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ConstructorEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return this.signature;
    }

    public ConstructorEntity signature(String signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getClassName() {
        return this.className;
    }

    public ConstructorEntity className(String pkg) {
        this.setClassName(pkg);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFile() {
        return this.file;
    }

    public ConstructorEntity file(String file) {
        this.setFile(file);
        return this;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Set<AttributeEntity> getAttributeEntities() {
        return this.attributeEntities;
    }

    public void setAttributeEntities(Set<AttributeEntity> attributeEntities) {
        this.attributeEntities = attributeEntities;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstructorEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((ConstructorEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConstructorEntity{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", signature='" + getSignature() + "'" +
            ", pkg='" + getClassName() + "'" +
            ", file='" + getFile() + "'" +
            "}";
    }
}
