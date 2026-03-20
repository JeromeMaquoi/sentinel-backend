package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.RunIterationDTO;

import java.util.List;

public class IterationRuntimeMeasurementsDTO {
    private RunIterationDTO iteration;
    private List<Double> runtimeValues;
    private Double totalEnergy;
    private List<Long> timestamps;

    public RunIterationDTO getIteration() {
        return iteration;
    }

    public void setIteration(RunIterationDTO iteration) {
        this.iteration = iteration;
    }

    public List<Double> getRuntimeValues() {
        return runtimeValues;
    }

    public void setRuntimeValues(List<Double> runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public Double getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(Double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public List<Long> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Long> timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public String toString() {
        return "IterationRuntimeMeasurements{" +
            "iteration=" + iteration +
            ", runtimeValues=" + runtimeValues +
            ", totalEnergy=" + totalEnergy +
            ", timestamps=" + timestamps +
            '}';
    }
}
