package com.snail.sentinel.backend.service.dto.aggregation;

import java.util.List;

/**
 * DTO for aggregated runtime call tree measurements grouped by callstack.
 * Contains per-iteration data with normalized and resampled time series values,
 * as well as aggregated values across all iterations on a fixed time grid.
 */
public class AggregatedRuntimeCallTreeMeasurementByIterationDTO {
    private List<String> callstack;
    private String type;
    private CommitAggregateDTO commit;

    // Per-iteration data: each iteration has a description and resampled values
    private List<IterationAggregateDTO> iterations;

    // Aggregated values across all iterations on a fixed time grid
    private List<Double> values;

    // Fixed time grid (normalized timestamps)
    private List<Long> timestamps;

    public AggregatedRuntimeCallTreeMeasurementByIterationDTO() {}

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

    public CommitAggregateDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitAggregateDTO commit) {
        this.commit = commit;
    }

    public List<IterationAggregateDTO> getIterations() {
        return iterations;
    }

    public void setIterations(List<IterationAggregateDTO> iterations) {
        this.iterations = iterations;
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

    @Override
    public String toString() {
        return "AggregatedRuntimeCallTreeMeasurementDTO{" +
            "callstack=" + callstack +
            ", type='" + type + '\'' +
            ", commit=" + commit +
            ", iterations=" + iterations +
            ", values=" + values +
            ", timestamps=" + timestamps +
            '}';
    }
}
