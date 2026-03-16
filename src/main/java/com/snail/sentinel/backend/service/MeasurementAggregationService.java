package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeDTO;

import java.util.List;

public interface MeasurementAggregationService {
    List<AggregatedRuntimeCallTreeDTO> aggregateRuntimeCallTree(String commitSha);
}
