package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;

import java.util.List;

public interface RuntimeCallTreeMeasurementService extends MeasurementService<RuntimeCallTreeMeasurementEntityDTO> {
    /**
     * Aggregates all measurements by callstack
     * @return A list of aggregated measurements
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstack();

    /**
     * Aggregates measurements by callstack for a specific commit SHA
     * @param commitSha commit sha used to filter the measurements
     * @return A list of aggregated measurements for the commit
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstackForCommit(String commitSha);

    /**
     * Aggregates measurements by callstack for a specific repository
     * @param repoName Name of the repository
     * @return A list of aggregated measurements for the repository
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstackForRepository(String repoName);

    /**
     * Aggregates measurements by callstack with flexible filtering
     * @param filter The aggregation filter criteria
     * @return A list of aggregated measurements matching the filter
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstack(MeasurementAggregationFilter filter);
}
