package com.snail.sentinel.backend.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A StackTraceElementJoularEntity.
 */
@Document(collection = "stack_trace_element_joular_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StackTraceElementJoularEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("line_number")
    private Integer lineNumber;

    @Field("constructor_element")
    private String constructorElement;

    @Field("parent")
    private String parent;

    @Field("ancestors")
    private String ancestors;

    @Field("consumption_values")
    private String consumptionValues;

    @Field("commit")
    private String commit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public StackTraceElementJoularEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public StackTraceElementJoularEntity lineNumber(Integer lineNumber) {
        this.setLineNumber(lineNumber);
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getConstructorElement() {
        return this.constructorElement;
    }

    public StackTraceElementJoularEntity constructorElement(String constructorElement) {
        this.setConstructorElement(constructorElement);
        return this;
    }

    public void setConstructorElement(String constructorElement) {
        this.constructorElement = constructorElement;
    }

    public String getParent() {
        return this.parent;
    }

    public StackTraceElementJoularEntity parent(String parent) {
        this.setParent(parent);
        return this;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAncestors() {
        return this.ancestors;
    }

    public StackTraceElementJoularEntity ancestors(String ancestors) {
        this.setAncestors(ancestors);
        return this;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getConsumptionValues() {
        return this.consumptionValues;
    }

    public StackTraceElementJoularEntity consumptionValues(String consumptionValues) {
        this.setConsumptionValues(consumptionValues);
        return this;
    }

    public void setConsumptionValues(String consumptionValues) {
        this.consumptionValues = consumptionValues;
    }

    public String getCommit() {
        return this.commit;
    }

    public StackTraceElementJoularEntity commit(String commit) {
        this.setCommit(commit);
        return this;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StackTraceElementJoularEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((StackTraceElementJoularEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StackTraceElementJoularEntity{" +
            "id=" + getId() +
            ", lineNumber=" + getLineNumber() +
            ", constructorElement='" + getConstructorElement() + "'" +
            ", parent='" + getParent() + "'" +
            ", ancestors='" + getAncestors() + "'" +
            ", consumptionValues='" + getConsumptionValues() + "'" +
            ", commit='" + getCommit() + "'" +
            "}";
    }
}
