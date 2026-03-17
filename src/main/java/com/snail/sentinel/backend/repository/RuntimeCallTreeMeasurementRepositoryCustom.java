package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;

import java.util.List;

public interface RuntimeCallTreeMeasurementRepositoryCustom {
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstack();
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstack(MeasurementAggregationFilter filter);
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstackAndCommitSha(String commitSha);
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateByCallstackAndRepositoryName(String repoName);
}
