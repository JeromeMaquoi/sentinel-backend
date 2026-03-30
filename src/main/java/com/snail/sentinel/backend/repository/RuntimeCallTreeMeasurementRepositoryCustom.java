package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuntimeCallTreeMeasurementRepositoryCustom {
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack();
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack(MeasurementAggregationFilter filter);
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackAndCommitSha(String commitSha);
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackAndRepositoryName(String repoName);
}
