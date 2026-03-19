package com.snail.sentinel.backend.service.dto.runtime;

import java.util.List;

public class RuntimeValuesDTO {
    private List<Double> values;

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    public RuntimeValuesDTO withValues(List<Double> values) {
        this.values = values;
        return this;
    }

    @Override
    public String toString() {
        return "RuntimeValuesDTO{" +
            "values=" + values +
            '}';
    }
}
