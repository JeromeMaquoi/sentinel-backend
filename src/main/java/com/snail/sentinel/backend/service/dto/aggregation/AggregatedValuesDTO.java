package com.snail.sentinel.backend.service.dto.aggregation;

import java.util.List;

public class AggregatedValuesDTO {
    private List<Double> values;

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
