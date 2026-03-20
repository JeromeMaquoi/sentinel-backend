package com.snail.sentinel.backend.repository.filter;

/**
 * Enum to define the type of aggregation filtering
 */
public enum AggregationFilterType {
    NONE,
    BY_COMMIT_SHA,
    BY_REPOSITORY_NAME
}
