package com.snail.sentinel.backend.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "joular_measurements")
@TypeAlias("calltree")
public class CallTreeMeasurementEntity extends AbstractEnergyMeasurement {
    private List<String> callstack;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }
}
