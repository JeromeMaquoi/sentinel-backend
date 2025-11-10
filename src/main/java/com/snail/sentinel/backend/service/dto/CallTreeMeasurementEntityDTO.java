package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.domain.MeasurementLevel;
import com.snail.sentinel.backend.domain.MonitoringType;
import com.snail.sentinel.backend.domain.Scope;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;

import java.io.Serializable;
import java.util.List;

public class CallTreeMeasurementEntityDTO implements Serializable {
    private String id;
    private Scope scope;
    private MeasurementLevel measurementLevel;
    private MonitoringType monitoringType;
    private RunIterationDTO iteration;
    private CommitSimpleDTO commit;
    private Float value;
    private List<String> callstack;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public MeasurementLevel getMeasurementType() {
        return measurementLevel;
    }

    public void setMeasurementType(MeasurementLevel measurementLevel) {
        this.measurementLevel = measurementLevel;
    }

    public MonitoringType getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(MonitoringType monitoringType) {
        this.monitoringType = monitoringType;
    }

    public RunIterationDTO getIteration() {
        return iteration;
    }

    public void setIteration(RunIterationDTO iteration) {
        this.iteration = iteration;
    }

    public CommitSimpleDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitSimpleDTO commit) {
        this.commit = commit;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }

    @Override
    public String toString() {
        return "CallTreeMeasurementEntityDTO{" +
            "id='" + id + '\'' +
            ", scope=" + scope +
            ", measurementType=" + measurementLevel +
            ", monitoringType=" + monitoringType +
            ", iteration=" + iteration +
            ", commit=" + commit +
            ", value=" + value +
            ", callstack=" + callstack +
            '}';
    }
}
