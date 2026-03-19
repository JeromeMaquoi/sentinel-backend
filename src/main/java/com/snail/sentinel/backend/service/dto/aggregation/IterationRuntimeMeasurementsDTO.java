package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.RunIterationDTO;
import com.snail.sentinel.backend.service.dto.runtime.RuntimeValuesDTO;
import com.snail.sentinel.backend.service.dto.runtime.TimestampsDTO;

public class IterationRuntimeMeasurementsDTO {
    private RunIterationDTO iteration;
    private RuntimeValuesDTO runtimeValues;
    private Double totalEnergy;
    private TimestampsDTO timestamps;

    public RunIterationDTO getIteration() {
        return iteration;
    }

    public void setIteration(RunIterationDTO iteration) {
        this.iteration = iteration;
    }

    public RuntimeValuesDTO getRuntimeValues() {
        return runtimeValues;
    }

    public void setRuntimeValues(RuntimeValuesDTO runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public Double getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(Double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public TimestampsDTO getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(TimestampsDTO timestamps) {
        this.timestamps = timestamps;
    }

    @Override
    public String toString() {
        return "IterationRuntimeMeasurements{" +
            "iteration=" + iteration +
            ", values=" + runtimeValues +
            ", totalEnergy=" + totalEnergy +
            ", timestamps=" + timestamps +
            '}';
    }
}
