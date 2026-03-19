package com.snail.sentinel.backend.repository.impl;

import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuntimeCallTreeMeasurementRepositoryImplTest {
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private AggregationResults<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregationResults;
    private RuntimeCallTreeMeasurementRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new RuntimeCallTreeMeasurementRepositoryImpl(mongoTemplate);
    }

    @Test
    void aggregateByCallstackWithouFilterCallsMongoTemplateTest() {
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResults = Collections.singletonList(new AggregatedRuntimeCallTreeMeasurementByIterationDTO());
        when(aggregationResults.getMappedResults()).thenReturn(expectedResults);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"), eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class))).thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> results = repository.aggregateByCallstack();

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallStackWithNoFilterCallsMongoTemplateTest() {
        MeasurementAggregationFilter filter = new MeasurementAggregationFilter();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResults = Collections.singletonList(new AggregatedRuntimeCallTreeMeasurementByIterationDTO());
        when(aggregationResults.getMappedResults()).thenReturn(expectedResults);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"), eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class))).thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> results =  repository.aggregateByCallstack(filter);

        assertNotNull(results);
        assertEquals(1, results.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallstackWithCommitShaFilterCallsMongoTemplateWithFilterTest() {
        String commitSha = "123abc123";
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byCommitSha(commitSha);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResults = Arrays.asList(
            new AggregatedRuntimeCallTreeMeasurementByIterationDTO(),
            new AggregatedRuntimeCallTreeMeasurementByIterationDTO()
        );
        when(aggregationResults.getMappedResults()).thenReturn(expectedResults);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"), eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class))).thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> results =  repository.aggregateByCallstack(filter);

        assertNotNull(results);
        assertEquals(2,  results.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallstackWithRepositoryNameFilterCallsMongoTemplateWithFilterTest() {
        String repoName = "repo-name";
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byRepositoryName(repoName);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(
            new AggregatedRuntimeCallTreeMeasurementByIterationDTO()
        );
        when(aggregationResults.getMappedResults()).thenReturn(expectedResult);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"),
            eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class)))
            .thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = repository.aggregateByCallstack(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallstackAndCommitShatCallsRepositoryWithCommitShaFilterTest() {
        String commitSha = "def456";
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(
            new AggregatedRuntimeCallTreeMeasurementByIterationDTO()
        );
        when(aggregationResults.getMappedResults()).thenReturn(expectedResult);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"),
            eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class)))
            .thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = repository.aggregateByCallstackAndCommitSha(commitSha);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallstackAndRepositoryNameCallsRepositoryWithRepositoryNameFilterTest() {
        String repoName = "spring-framework";
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(
            new AggregatedRuntimeCallTreeMeasurementByIterationDTO()
        );
        when(aggregationResults.getMappedResults()).thenReturn(expectedResult);
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"),
            eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class)))
            .thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = repository.aggregateByCallstackAndRepositoryName(repoName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
    }

    @Test
    void aggregateByCallstackEmptyResultReturnsEmptyListTest() {
        when(aggregationResults.getMappedResults()).thenReturn(Collections.emptyList());
        when(mongoTemplate.aggregate(any(Aggregation.class), eq("joularjx_measurements"),
            eq(AggregatedRuntimeCallTreeMeasurementByIterationDTO.class)))
            .thenReturn(aggregationResults);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = repository.aggregateByCallstack();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
