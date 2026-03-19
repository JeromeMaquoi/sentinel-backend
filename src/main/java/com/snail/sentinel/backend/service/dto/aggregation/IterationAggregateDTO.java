package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.RunIterationDTO;

import java.util.List;

/**
 * Represents aggregated data for a single iteration within an aggregated runtime call tree measurement.
 * Contains the iteration and its corresponding resampled values aligned to a fixed time grid.
 */
public class IterationAggregateDTO {
    private RunIterationDTO description;
    private List<Double> values;

    public IterationAggregateDTO() {}

    public IterationAggregateDTO(RunIterationDTO description, List<Double> values) {
        this.description = description;
        this.values = values;
    }

    public RunIterationDTO getDescription() {
        return description;
    }

    public void setDescription(RunIterationDTO description) {
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

