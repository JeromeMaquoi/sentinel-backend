package com.snail.sentinel.backend.service.dto;

import java.util.List;

public class RuntimeCallTreeMeasurementEntityDTO extends RuntimeMeasurementEntityDTO {
    private List<String> callstack;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }
}
