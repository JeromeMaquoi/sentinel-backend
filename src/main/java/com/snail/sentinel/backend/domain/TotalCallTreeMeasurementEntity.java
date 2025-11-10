package com.snail.sentinel.backend.domain;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Document(collection = "joularjx_measurements")
@TypeAlias("total_calltree")
public class TotalCallTreeMeasurementEntity extends TotalMeasurementEntity {
    private List<String> callstack;

    public List<String> getCallstack() {
        return callstack;
    }

    public void setCallstack(List<String> callstack) {
        this.callstack = callstack;
    }
}
