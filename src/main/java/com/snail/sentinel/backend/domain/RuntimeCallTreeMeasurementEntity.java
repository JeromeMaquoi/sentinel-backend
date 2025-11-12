package com.snail.sentinel.backend.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "joularjx_measurements")
@TypeAlias("runtime_calltree")
public class RuntimeCallTreeMeasurementEntity extends RuntimeMeasurementEntity {
    private List<String> callstack;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }
}
