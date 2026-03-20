package com.snail.sentinel.backend.repository.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementAggregationFilterTest {
    @Test
    void byCommitShaCreatesFilterWithCorrectValuesTest() {
        String commitSha = "abc12345";
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byCommitSha(commitSha);

        assertEquals(AggregationFilterType.BY_COMMIT_SHA, filter.getFilterType());
        assertEquals(commitSha, filter.getFilterValue());
        assertTrue(filter.hasFilter());
    }

    @Test
    void byRepositoryNameCreatesFilterWithCorrectValuesTest() {
        String repositoryName = "abc";
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byRepositoryName(repositoryName);

        assertEquals(AggregationFilterType.BY_REPOSITORY_NAME, filter.getFilterType());
        assertEquals(repositoryName, filter.getFilterValue());
        assertTrue(filter.hasFilter());
    }

    @Test
    void hasFilterReturnsFalseWhenFilterTypeIsNoneTest() {
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        filter.setFilterType(AggregationFilterType.NONE);
        filter.setFilterValue("some value");

        assertFalse(filter.hasFilter());
    }

    @Test
    void hasFilterReturnsFalseWhenFilterValueIsNullTest() {
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        filter.setFilterType(AggregationFilterType.BY_COMMIT_SHA);
        filter.setFilterValue(null);

        assertFalse(filter.hasFilter());
    }

    @Test
    void hasFitlerReturnsTrueWhenFilterIsValidTest() {
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byCommitSha("abc123");
        assertTrue(filter.hasFilter());
    }
}
