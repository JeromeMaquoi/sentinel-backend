package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeWithConstructorsDTO;

import java.util.List;

public interface RuntimeCallTreeMeasurementService extends MeasurementService<RuntimeCallTreeMeasurementEntityDTO> {
    /**
     * Aggregates all measurements by callstack
     * @return A list of aggregated measurements
     */
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack();

    /**
     * Aggregates measurements by callstack for a specific commit SHA
     * @param commitSha commit sha used to filter the measurements
     * @return A list of aggregated measurements for the commit
     */
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackForCommit(String commitSha);

    /**
     * Aggregates measurements by callstack for a specific repository
     * @param repoName Name of the repository
     * @return A list of aggregated measurements for the repository
     */
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackForRepository(String repoName);

    /**
     * Aggregates measurements by callstack with flexible filtering
     * @param filter The aggregation filter criteria
     * @return A list of aggregated measurements matching the filter
     */
    List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack(MeasurementAggregationFilter filter);

    /**
     * Aggregates all measurements across all iterations by callstack
     * @return A list of aggregated measurements, aggregated across all iterations for each callstack
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstack();

    /**
     * Aggregates all measurements across all iterations by callstack with optional minimum iteration count filtering
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements filtered by minimum iteration count
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstack(Integer minIterations);

    /**
     * Aggregates measurements across all iterations by callstack for a specific commit SHA
     * @param commitSha commit sha used to filter the measurements
     * @return A list of aggregated measurements for the commit, aggregated across all iterations
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForCommit(String commitSha);

    /**
     * Aggregates measurements across all iterations by callstack for a specific commit SHA with optional minimum iteration count filtering
     * @param commitSha commit sha used to filter the measurements
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements filtered by minimum iteration count
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForCommit(String commitSha, Integer minIterations);

    /**
     * Aggregates measurements across all iterations by callstack for a specific repository
     * @param repoName Name of the repository
     * @return A list of aggregated measurements for the repository, aggregated across all iterations
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForRepository(String repoName);

    /**
     * Aggregates measurements across all iterations by callstack for a specific repository with optional minimum iteration count filtering
     * @param repoName Name of the repository
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements filtered by minimum iteration count
     */
    List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForRepository(String repoName, Integer minIterations);

    /**
     * Finds constructors matching the callstacks in aggregated measurements across all iterations.
     * Returns aggregated measurements enriched with matching constructor context.
     *
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements with matched constructors filtered by minimum iteration count
     */
    List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacks(Integer minIterations);

    /**
     * Finds constructors matching the callstacks in aggregated measurements for a specific commit.
     * Returns aggregated measurements enriched with matching constructor context.
     *
     * @param commitSha the commit SHA to filter by
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements with matched constructors for the commit
     */
    List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacksForCommit(String commitSha, Integer minIterations);

    /**
     * Finds constructors matching the callstacks in aggregated measurements for a specific repository.
     * Returns aggregated measurements enriched with matching constructor context.
     *
     * @param repoName the repository name to filter by
     * @param minIterations optional minimum number of iterations required (null means no minimum filter)
     * @return A list of aggregated measurements with matched constructors for the repository
     */
    List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacksForRepository(String repoName, Integer minIterations);
}
