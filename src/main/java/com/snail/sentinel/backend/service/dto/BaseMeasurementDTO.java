package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.domain.MeasurementLevel;
import com.snail.sentinel.backend.domain.MonitoringType;
import com.snail.sentinel.backend.domain.Scope;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;

public class BaseMeasurementDTO {
    private String id;
    private Scope scope;
    private MeasurementLevel measurementLevel;
    private MonitoringType monitoringType;
    private RunIterationDTO iteration;
    private CommitSimpleDTO commit;
    private Float value;

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

    public MeasurementLevel getMeasurementLevel() {
        return measurementLevel;
    }

    public void setMeasurementLevel(MeasurementLevel measurementLevel) {
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
}
