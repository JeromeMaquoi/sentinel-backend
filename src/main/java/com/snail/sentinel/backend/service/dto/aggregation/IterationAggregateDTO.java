package com.snail.sentinel.backend.service.dto.aggregation;

import java.util.List;

/**
 * Represents aggregated data for a single iteration within an aggregated runtime call tree measurement.
 * Contains descriptions of the iteration and its corresponding resampled values aligned to a fixed time grid.
 */
public class IterationAggregateDTO {
    private String description;
    private List<Double> values;

    public IterationAggregateDTO() {}

    public IterationAggregateDTO(String description, List<Double> values) {
        this.description = description;
        this.values = values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "IterationAggregateDTO{" +
            "description='" + description + '\'' +
            ", values=" + values +
            '}';
    }
}

