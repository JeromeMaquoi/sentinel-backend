package com.snail.sentinel.backend.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A JoularNodeEntity.
 */
@Document(collection = "joular_node_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JoularNodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("line_number")
    private Integer lineNumber;

    @Field("value")
    private Float value;

    @Field("measurableElement")
    private transient MeasurableElementDTO measurableElement;

    @Field("iteration")
    private transient IterationDTO iteration;

    @Field("commit")
    private transient CommitSimpleDTO commit;

    @Field("ancestors")
    private List<String> ancestors;

    @Field("parent")
    private String parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public JoularNodeEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public JoularNodeEntity lineNumber(Integer lineNumber) {
        this.setLineNumber(lineNumber);
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Float getValue() {
        return this.value;
    }

    public JoularNodeEntity value(Float value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularNodeEntity that = (JoularNodeEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(lineNumber, that.lineNumber) && Objects.equals(value, that.value) && Objects.equals(measurableElement, that.measurableElement) && Objects.equals(iteration, that.iteration) && Objects.equals(commit, that.commit) && Objects.equals(ancestors, that.ancestors) && Objects.equals(parent, that.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineNumber, value, measurableElement, iteration, commit, ancestors, parent);
    }

    @Override
    public String toString() {
        return "JoularNodeEntity{" +
            "id='" + id + '\'' +
            ", lineNumber=" + lineNumber +
            ", value=" + value +
            ", measurableElement=" + measurableElement +
            ", iteration=" + iteration +
            ", commit=" + commit +
            ", ancestors=" + ancestors +
            ", parent='" + parent + '\'' +
            '}';
    }
}
