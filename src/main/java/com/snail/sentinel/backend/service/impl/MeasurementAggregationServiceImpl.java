package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementAggregationService;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeasurementAggregationServiceImpl implements MeasurementAggregationService {
    private static final Logger log = LoggerFactory.getLogger(MeasurementAggregationServiceImpl.class);

    private final RuntimeCallTreeMeasurementRepository runtimeCallTreeRepo;

    public MeasurementAggregationServiceImpl(RuntimeCallTreeMeasurementRepository runtimeCallTreeRepo) {
        this.runtimeCallTreeRepo = runtimeCallTreeRepo;
    }

    @Override
    public List<AggregatedRuntimeCallTreeDTO> aggregateRuntimeCallTree(String commitSha) {
        log.debug("Aggregating RuntimeCallTree measurements for commit {}", commitSha);

        List<RuntimeCallTreeMeasurementEntity> measurements = runtimeCallTreeRepo.findByCommitSha(commitSha);

        if (measurements == null || measurements.isEmpty()) {
            log.warn("No RuntimeCallTree measurements found for commit {}", commitSha);
            return Collections.emptyList();
        }

        Map<String, List<RuntimeCallTreeMeasurementEntity>> groupedByCallstack = measurements.stream()
            .collect(Collectors.groupingBy(m -> String.join("|", m.getCallstack())));

        return groupedByCallstack.entrySet().stream()
            .map(entry -> createAggregatedRuntimeCallTree(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    protected AggregatedRuntimeCallTreeDTO createAggregatedRuntimeCallTree(String callstackKey, List<RuntimeCallTreeMeasurementEntity> measurements) {
        AggregatedRuntimeCallTreeDTO result = new  AggregatedRuntimeCallTreeDTO();

        RuntimeCallTreeMeasurementEntity first =  measurements.get(0);
        result.setCallstack(first.getCallstack());
        result.setType(first.getType());
    }
}
