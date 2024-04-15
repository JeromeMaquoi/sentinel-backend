package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.JoularNodeEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JoularNodeEntityDTO implements Serializable {

    private String id;

    private Integer lineNumber;

    private Float value;

    private String scope;

    private String monitoringType;

    private transient MeasurableElementDTO measurableElement;

    private transient IterationDTO iteration;

    private transient CommitSimpleDTO commit;

    private List<String> ancestors;

    private String parent;

    public String getLabel() {
        return this.measurableElement.getClassMethodSignature() + " " + this.lineNumber;
    }

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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public MeasurableElementDTO getMeasurableElement() {
        return measurableElement;
    }

    public void setMeasurableElement(MeasurableElementDTO measurableElement) {
        this.measurableElement = measurableElement;
    }

    public IterationDTO getIteration() {
        return iteration;
    }

    public void setIteration(IterationDTO iteration) {
        this.iteration = iteration;
    }

    public CommitSimpleDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitSimpleDTO commit) {
        this.commit = commit;
    }

    public List<String> getAncestors() {
        return ancestors;
    }

    public void setAncestors(List<String> ancestors) {
        this.ancestors = ancestors;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularNodeEntityDTO that = (JoularNodeEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(lineNumber, that.lineNumber) && Objects.equals(value, that.value) && Objects.equals(scope, that.scope) && Objects.equals(monitoringType, that.monitoringType) && Objects.equals(measurableElement, that.measurableElement) && Objects.equals(iteration, that.iteration) && Objects.equals(commit, that.commit) && Objects.equals(ancestors, that.ancestors) && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineNumber, value, scope, monitoringType, measurableElement, iteration, commit, ancestors, parent);
    }

    @Override
    public String toString() {
        return "JoularNodeEntityDTO{" +
            "id='" + id + '\'' +
            ", lineNumber=" + lineNumber +
            ", value=" + value +
            ", scope='" + scope + '\'' +
            ", monitoringType='" + monitoringType + '\'' +
            ", measurableElement=" + measurableElement +
            ", iteration=" + iteration +
            ", commit=" + commit +
            ", ancestors=" + ancestors +
            ", parent='" + parent + '\'' +
            '}';
    }
}
