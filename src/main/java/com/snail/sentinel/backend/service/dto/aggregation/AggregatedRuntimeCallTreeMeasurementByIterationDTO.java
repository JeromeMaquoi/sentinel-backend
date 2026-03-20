package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.RunIterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;

import java.util.List;

public class AggregatedRuntimeCallTreeMeasurementByIterationDTO {
    private List<String> callstack;
    String scope;
    private String type;
    private RunIterationDTO iteration;
    private CommitSimpleDTO commit;
    private List<Double> values;
    private List<Long> timestamps;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
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

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public RunIterationDTO getIteration() {
        return iteration;
    }

    public void setIteration(RunIterationDTO iteration) {
        this.iteration = iteration;
    }

    @Override
    public String toString() {
        return "AggregatedRuntimeCallTreeMeasurementByIterationDTO{" +
            "callstack=" + callstack +
            ", scope='" + scope + '\'' +
            ", type='" + type + '\'' +
            ", iteration=" + iteration +
            ", commit=" + commit +
            ", values=" + values +
            ", timestamps=" + timestamps +
            '}';
    }
}


