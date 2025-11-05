package com.snail.sentinel.backend.domain;


import com.snail.sentinel.backend.service.dto.RunIterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import org.springframework.data.annotation.Id;

public abstract class AbstractEnergyMeasurement {
    @Id
    private String id;
    private Scope scope;
    private MeasurementType measurementType;
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

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(MeasurementType measurementType) {
        this.measurementType = measurementType;
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
