package com.snail.sentinel.backend.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.JoularNodeEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JoularNodeEntityDTO implements Serializable {

    private String id;

    private Integer lineNumber;

    private Float value;

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

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoularNodeEntityDTO)) {
            return false;
        }

        JoularNodeEntityDTO joularNodeEntityDTO = (JoularNodeEntityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, joularNodeEntityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JoularNodeEntityDTO{" +
            "id='" + getId() + "'" +
            ", lineNumber=" + getLineNumber() +
            ", value=" + getValue() +
            "}";
    }
}
