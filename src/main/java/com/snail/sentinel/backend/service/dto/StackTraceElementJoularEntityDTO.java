package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.service.dto.measurableelement.ConstructorElementDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.StackTraceElementJoularEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StackTraceElementJoularEntityDTO implements Serializable {

    private String id;

    private Integer lineNumber;

    private ConstructorElementDTO constructorElement;

    private String parent;

    private String ancestors;

    private String consumptionValues;

    private String commit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public ConstructorElementDTO getConstructorElement() {
        return constructorElement;
    }

    public void setConstructorElement(ConstructorElementDTO constructorElement) {
        this.constructorElement = constructorElement;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getConsumptionValues() {
        return consumptionValues;
    }

    public void setConsumptionValues(String consumptionValues) {
        this.consumptionValues = consumptionValues;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StackTraceElementJoularEntityDTO)) {
            return false;
        }

        StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO = (StackTraceElementJoularEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stackTraceElementJoularEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StackTraceElementJoularEntityDTO{" +
            "id='" + getId() + "'" +
            ", lineNumber=" + getLineNumber() +
            ", constructorElement='" + getConstructorElement() + "'" +
            ", parent='" + getParent() + "'" +
            ", ancestors='" + getAncestors() + "'" +
            ", consumptionValues='" + getConsumptionValues() + "'" +
            ", commit='" + getCommit() + "'" +
            "}";
    }
}
