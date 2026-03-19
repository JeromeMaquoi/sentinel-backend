package com.snail.sentinel.backend.repository.impl;

import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepositoryCustom;
import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class RuntimeCallTreeMeasurementRepositoryImpl implements RuntimeCallTreeMeasurementRepositoryCustom {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeMeasurementRepositoryImpl.class);
    private static final String RUNTIME_CALLTREE_CLASS = "runtime_calltree";
    private final MongoTemplate mongoTemplate;

    public RuntimeCallTreeMeasurementRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack() {
        log.debug("Aggregating RuntimeCallTreeMeasurements by callstack without filter");
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        return aggregateByCallstack(filter);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack(MeasurementAggregationFilter filter) {
        log.debug("Aggregating RuntimeCallTreeMeasurements by callstack with filter {}", filter.getFilterType());

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(Criteria.where("_class").is(RUNTIME_CALLTREE_CLASS)));

        if (filter.hasFilter()) {
            operations.add(buildMatchOperation(filter));
        }
        operations.add(buildGroupOperation());

        Aggregation aggregation = newAggregation(operations)
            .withOptions(AggregationOptions.builder()
                .allowDiskUse(true)
                .cursorBatchSize(1000)
            .build());
        try {
            AggregationResults<AggregatedRuntimeCallTreeMeasurementByIterationDTO> results = mongoTemplate.aggregate(aggregation, "joularjx_measurements", AggregatedRuntimeCallTreeMeasurementByIterationDTO.class);
            return results.getMappedResults();
        } catch (Exception e) {
            log.error("Error during aggregation: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackAndCommitSha(String commitSha) {
        log.debug("Aggregating RuntimeCallTreeMeasurements by callstack and commit SHA {}", commitSha);
        return aggregateByCallstack(MeasurementAggregationFilter.byCommitSha(commitSha));
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackAndRepositoryName(String repoName) {
        log.debug("Aggregating RuntimeCallTreeMeasurements by callstack and repository name {}", repoName);
        return aggregateByCallstack(MeasurementAggregationFilter.byRepositoryName(repoName));
    }

    /**
     * Builds a match operation based on the filter type
     * @param filter Filter used to filter the data
     * @return A match operation using the filter
     */
    protected MatchOperation buildMatchOperation(MeasurementAggregationFilter filter) {
        Criteria criteria = switch (filter.getFilterType()) {
            case BY_COMMIT_SHA -> Criteria.where("commit.sha").is(filter.getFilterValue());
            case BY_REPOSITORY_NAME -> Criteria.where("commit.repository.name").is(filter.getFilterValue());
            default -> throw new IllegalArgumentException("Unsupported filter type: " + filter.getFilterType());
        };
        return match(criteria);
    }

    protected GroupOperation buildGroupOperation() {
        return group("callstack")
            .push("timestamp").as("timestamps")
            .first("callstack").as("callstack")
            .push("iteration").as("iterations")
            .first("commit").as("commit")
            .push("value").as("values")
            .first("_class").as("type");
    }
}
