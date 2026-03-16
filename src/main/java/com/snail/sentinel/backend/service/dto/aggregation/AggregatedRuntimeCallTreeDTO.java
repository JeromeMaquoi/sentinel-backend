package com.snail.sentinel.backend.service.dto.aggregation;

import java.util.List;

public class AggregatedRuntimeCallTreeDTO {
    private List<String> callstack;
    private String type;
    private CommitAggregateDTO commit;
    private AggregatedValuesDTO aggregatedValues;
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

    public CommitAggregateDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitAggregateDTO commit) {
        this.commit = commit;
    }

    public AggregatedValuesDTO getAggregatedValues() {
        return aggregatedValues;
    }

    public void setAggregatedValues(AggregatedValuesDTO aggregatedValues) {
        this.aggregatedValues = aggregatedValues;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }
}
