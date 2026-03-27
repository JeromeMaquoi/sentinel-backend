package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.MatchedConstructorDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;

import java.util.List;

/**
 * Service for matching constructor calls in runtime callstacks to ConstructorContextEntity documents.
 *
 * This service uses a hybrid approach:
 * 1. Extracts constructor calls (identified by <init> method names) from runtime callstacks
 * 2. Filters ConstructorContextEntity documents by matching class names
 * 3. Validates matches by checking if the constructor's stacktrace appears as a subsequence
 *    in the runtime callstack
 */
public interface ConstructorCallstackMatcher {

    /**
     * Finds ConstructorContextEntity documents that match constructor calls within the aggregated runtime callstack.
     *
     * @param aggregatedMeasurement The aggregated runtime callstack measurement containing a callstack
     * @return A list of MatchedConstructorDTO objects containing matched constructors and their positions in the callstack
     */
    List<MatchedConstructorDTO> findMatchingConstructors(AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement);

    /**
     * Finds ConstructorContextEntity documents that match constructor calls within the aggregated runtime callstack.
     *
     * @param callstack The runtime callstack
     * @return A list of MatchedConstructorDTO objects containing matched constructors and their positions in the callstack
     */
    List<MatchedConstructorDTO> findMatchingConstructors(List<String> callstack);

    /**
     * Finds ConstructorContextEntity documents that match constructor calls within the aggregated runtime callstack,
     * filtered by a specific commit SHA.
     *
     * @param callstack The runtime callstack
     * @param commitSha The commit SHA to filter constructors by (can be null to search all constructors)
     * @return A list of MatchedConstructorDTO objects containing matched constructors and their positions in the callstack
     */
    List<MatchedConstructorDTO> findMatchingConstructors(List<String> callstack, String commitSha);

    /**
     * Finds ConstructorContextEntity documents that match constructor calls within the aggregated runtime callstack,
     * filtered by a specific repository name.
     *
     * @param callstack The runtime callstack
     * @param repositoryName The repository name to filter constructors by (can be null to search all constructors)
     * @return A list of MatchedConstructorDTO objects containing matched constructors and their positions in the callstack
     */
    List<MatchedConstructorDTO> findMatchingConstructorsByRepository(List<String> callstack, String repositoryName);

}

