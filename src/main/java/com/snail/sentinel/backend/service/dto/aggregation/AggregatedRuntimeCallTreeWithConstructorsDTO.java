package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;

import java.util.Map;

/**
 * DTO that combines an aggregated runtime callstack measurement with its matched constructors.
 * This is used to return constructor context information for constructor calls found in a callstack.
 */
public class AggregatedRuntimeCallTreeWithConstructorsDTO {

    /**
     * The original aggregated runtime callstack measurement
     */
    private AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement;

    /**
     * Map of matched constructors from the callstack
     * Key format: "className#<init>(param1,param2,...)"
     * Value: The ConstructorContextEntity with full context about the constructor
     */
    private Map<String, ConstructorContextEntity> matchedConstructors;

    public AggregatedRuntimeCallTreeWithConstructorsDTO() {
    }

    public AggregatedRuntimeCallTreeWithConstructorsDTO(
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement,
        Map<String, ConstructorContextEntity> matchedConstructors) {
        this.aggregatedMeasurement = aggregatedMeasurement;
        this.matchedConstructors = matchedConstructors;
    }

    public AggregatedRuntimeCallTreeMeasurementDTO getAggregatedMeasurement() {
        return aggregatedMeasurement;
    }

    public void setAggregatedMeasurement(AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement) {
        this.aggregatedMeasurement = aggregatedMeasurement;
    }

    public Map<String, ConstructorContextEntity> getMatchedConstructors() {
        return matchedConstructors;
    }

    public void setMatchedConstructors(Map<String, ConstructorContextEntity> matchedConstructors) {
        this.matchedConstructors = matchedConstructors;
    }

    @Override
    public String toString() {
        return "AggregatedRuntimeCallTreeWithConstructorsDTO{" +
            "aggregatedMeasurement=" + aggregatedMeasurement +
            ", matchedConstructors=" + (matchedConstructors != null ? matchedConstructors.keySet() : null) +
            '}';
    }
}


