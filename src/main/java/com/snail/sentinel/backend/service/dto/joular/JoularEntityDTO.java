package com.snail.sentinel.backend.service.dto.joular;

import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementDTO;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class JoularEntityDTO implements Serializable {
    private String id;

    @NotNull
    private Float value;

    @NotNull
    private String scope;

    @NotNull
    private String monitoringType;

    @NotNull
    private transient MethodElementDTO methodElementDTO;

    @NotNull
    private transient IterationDTO iterationDTO;

    @NotNull
    private transient CommitSimpleDTO commitSimpleDTO;

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

    public MethodElementDTO getMethodElementDTO() {
        return methodElementDTO;
    }

    public void setMethodElementDTO(MethodElementDTO methodElementDTO) {
        this.methodElementDTO = methodElementDTO;
    }

    public IterationDTO getIterationDTO() {
        return iterationDTO;
    }

    public void setIterationDTO(IterationDTO iterationDTO) {
        this.iterationDTO = iterationDTO;
    }

    public CommitSimpleDTO getCommitSimpleDTO() {
        return commitSimpleDTO;
    }

    public void setCommitSimpleDTO(CommitSimpleDTO commitSimpleDTO) {
        this.commitSimpleDTO = commitSimpleDTO;
    }

    public Boolean hasMethodElement(MethodElementDTO methodElementDTO) {
        return this.methodElementDTO.equals(methodElementDTO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoularEntityDTO that = (JoularEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(scope, that.scope) && Objects.equals(monitoringType, that.monitoringType) && Objects.equals(methodElementDTO, that.methodElementDTO) && Objects.equals(iterationDTO, that.iterationDTO) && Objects.equals(commitSimpleDTO, that.commitSimpleDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, scope, monitoringType, methodElementDTO, iterationDTO, commitSimpleDTO);
    }

    @Override
    public String toString() {
        return "JoularEntityDTO{" +
            "id='" + id + '\'' +
            ", value=" + value +
            ", scope='" + scope + '\'' +
            ", monitoringType='" + monitoringType + '\'' +
            ", methodElementDTO=" + methodElementDTO +
            ", iterationDTO=" + iterationDTO +
            ", commitSimpleDTO=" + commitSimpleDTO +
            '}';
    }
}
