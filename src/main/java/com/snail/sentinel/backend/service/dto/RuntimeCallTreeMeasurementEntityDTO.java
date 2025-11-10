package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.domain.MeasurementLevel;
import com.snail.sentinel.backend.domain.MonitoringType;
import com.snail.sentinel.backend.domain.Scope;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;

import java.io.Serializable;
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
