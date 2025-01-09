package com.snail.sentinel.backend.service.dto.measurableelement;

import com.snail.sentinel.backend.service.dto.AttributeEntityDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

public class ConstructorElementDTO extends MeasurableElementDTO {
    @NotNull
    private List<AttributeEntityDTO> attributes;

    public List<AttributeEntityDTO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeEntityDTO> attributes) {
        this.attributes = attributes;
    }

    public ConstructorElementDTO attributes(List<AttributeEntityDTO> attributes) {
        this.setAttributes(attributes);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ConstructorElementDTO that = (ConstructorElementDTO) o;
        return Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attributes);
    }

    @Override
    public String toString() {
        return "ConstructorElementDTO{" +
            "attributes=" + attributes +
            '}';
    }
}
