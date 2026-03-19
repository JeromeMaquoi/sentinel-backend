package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;

import java.util.List;

public class AggregatedRuntimeCallTreeMeasurementDTO {
    private List<String> callstack;
    private String scope;
    private String type;
    private CommitSimpleDTO commit;
    private IterationRuntimeMeasurementsDTO measurements;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CommitSimpleDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitSimpleDTO commit) {
        this.commit = commit;
    }

    public IterationRuntimeMeasurementsDTO getMeasurements() {
        return measurements;
    }

    public void setMeasurements(IterationRuntimeMeasurementsDTO measurements) {
        this.measurements = measurements;
    }

    @Override
    public String toString() {
        return "AggregatedRuntimeCallTreeMeasurementDTO{" +
            "callstack=" + callstack +
            ", scope='" + scope + '\'' +
            ", type='" + type + '\'' +
            ", commit=" + commit +
            ", measurements=" + measurements +
            '}';
    }
}
