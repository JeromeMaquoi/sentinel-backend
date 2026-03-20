package com.snail.sentinel.backend.repository.filter;

public class MeasurementAggregationFilter {
    private AggregationFilterType filterType;
    private String filterValue;

    public MeasurementAggregationFilter() {
        this.filterType = AggregationFilterType.NONE;
    }

    public static MeasurementAggregationFilter byCommitSha(String commitSha) {
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        filter.filterType = AggregationFilterType.BY_COMMIT_SHA;
        filter.filterValue = commitSha;
        return filter;
    }

    public static MeasurementAggregationFilter byRepositoryName(String repoName) {
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        filter.filterType = AggregationFilterType.BY_REPOSITORY_NAME;
        filter.filterValue = repoName;
        return filter;
    }

    public AggregationFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(AggregationFilterType filterType) {
        this.filterType = filterType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public boolean hasFilter() {
        return filterType != AggregationFilterType.NONE && filterValue != null && !filterValue.isEmpty();
    }
}
