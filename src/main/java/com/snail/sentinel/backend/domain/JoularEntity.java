package com.snail.sentinel.backend.domain;

import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "joular_entity")
public class JoularEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("value")
    private Float value;

    @NotNull
    @Field("scope")
    private String scope;

    @NotNull
    @Field("monitoringType")
    private String monitoringType;

    @NotNull
    @Field("measurableElement")
    private transient MeasurableElementDTO measurableElement;

    @NotNull
    @Field("iteration")
    private transient IterationDTO iteration;

    @NotNull
    @Field("commit")
    private transient CommitSimpleDTO commit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularEntity that = (JoularEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(scope, that.scope) && Objects.equals(monitoringType, that.monitoringType) && Objects.equals(measurableElement, that.measurableElement) && Objects.equals(iteration, that.iteration) && Objects.equals(commit, that.commit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, scope, monitoringType, measurableElement, iteration, commit);
    }

    @Override
    public String toString() {
        return "JoularEntity{" +
            "id='" + id + '\'' +
            ", value=" + value +
            ", scope='" + scope + '\'' +
            ", monitoringType='" + monitoringType + '\'' +
            ", measurableElement=" + measurableElement +
            ", iteration=" + iteration +
            ", commit=" + commit +
            '}';
    }
}
