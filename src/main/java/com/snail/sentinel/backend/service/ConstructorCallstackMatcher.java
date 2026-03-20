package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;

import java.util.List;
import java.util.Map;

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
     * @return A map where keys are matched constructor identifiers and values are the matching ConstructorContextEntity documents
     */
    Map<String, ConstructorContextEntity> findMatchingConstructors(AggregatedRuntimeCallTreeMeasurementDTO aggregatedMeasurement);

    /**
     * Finds ConstructorContextEntity documents that match constructor calls within the aggregated runtime callstack.
     *
     * @param callstack The runtime callstack
     * @return A map where keys are matched constructor identifiers and values are the matching ConstructorContextEntity documents
     */
    Map<String, ConstructorContextEntity> findMatchingConstructors(List<String> callstack);

    /**
     * Checks if a constructor's stacktrace appears as a subsequence in the given runtime callstack.
     *
     * @param runtimeCallstack The runtime callstack (sequence of method names)
     * @param constructor The constructor with its stacktrace to match
     * @return true if the constructor's stacktrace is a subsequence of the runtime callstack, false otherwise
     */
    boolean isStacktraceSubsequence(List<String> runtimeCallstack, ConstructorContextEntity constructor);
}

