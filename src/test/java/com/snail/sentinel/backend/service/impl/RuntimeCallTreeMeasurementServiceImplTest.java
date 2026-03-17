package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.mapper.RuntimeCallTreeMeasurementEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class RuntimeCallTreeMeasurementServiceImplTest {
    @Mock
    private RuntimeCallTreeMeasurementRepository repository;

    @Mock
    private RuntimeCallTreeMeasurementEntityMapper mapper;

    @InjectMocks
    private RuntimeCallTreeMeasurementServiceImpl service;

    private RuntimeCallTreeMeasurementEntity entity;
    private RuntimeCallTreeMeasurementEntityDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new RuntimeCallTreeMeasurementEntity();
        entity.setId("id1");
        entity.setValue(0.5f);
        dto = new RuntimeCallTreeMeasurementEntityDTO();
        dto.setId("id1");
        dto.setValue(0.5f);
    }

    @Test
    void saveTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RuntimeCallTreeMeasurementEntityDTO result = service.save(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void bulkAddTest() {
        List<RuntimeCallTreeMeasurementEntityDTO> dtoList = List.of(dto);
        List<RuntimeCallTreeMeasurementEntity> entityList = List.of(entity);

        when(mapper.toEntity(dtoList)).thenReturn(entityList);
        when(repository.insert(entityList)).thenReturn(entityList);
        when(mapper.toDto(entityList)).thenReturn(dtoList);

        List<RuntimeCallTreeMeasurementEntityDTO> result = service.bulkAdd(dtoList);

        assertEquals(dtoList, result);
        verify(repository).insert(entityList);
    }

    @Test
    void updateTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RuntimeCallTreeMeasurementEntityDTO result = service.update(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            RuntimeCallTreeMeasurementEntity existingEntity = invocation.getArgument(0);
            RuntimeCallTreeMeasurementEntityDTO dtoArg = invocation.getArgument(1);
            existingEntity.setId(dtoArg.getId());
            return null;
        }).when(mapper).partialUpdate(entity, dto);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<RuntimeCallTreeMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isPresent();
        assertEquals(dto, result.orElseThrow());
        verify(repository).findById("id1");
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateNotFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        Optional<RuntimeCallTreeMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isEmpty();
        verify(repository).findById("id1");
        verify(repository, never()).save(any());
    }

    @Test
    void findAllTest() {
        List<RuntimeCallTreeMeasurementEntity> entityList = List.of(entity);

        when(repository.findAll()).thenReturn(entityList);
        when(mapper.toDto(entity)).thenReturn(dto);

        List<RuntimeCallTreeMeasurementEntityDTO> result = service.findAll();

        assertThat(result).containsExactly(dto);
        verify(repository).findAll();
    }

    @Test
    void findOneTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<RuntimeCallTreeMeasurementEntityDTO> result = service.findOne("id1");

        assertThat(result).isPresent();
        assertEquals(dto, result.orElseThrow());
        verify(repository).findById("id1");
    }

    @Test
    void deleteTest() {
        doNothing().when(repository).deleteById("id1");
        service.delete("id1");
        verify(repository).deleteById("id1");
    }

    @Test
    void aggregateByCallstackCallsRepositoryMethodTest() {
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedDTO = new AggregatedRuntimeCallTreeMeasurementDTO();
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregatedDTO);
        when(repository.aggregateByCallstack()).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDTO, result.get(0));
        verify(repository, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackForCommitCallsRepositoryWithCommitShaTest() {
        String commitSha = "123abc123";
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedDTO = new AggregatedRuntimeCallTreeMeasurementDTO();
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregatedDTO);
        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstackForCommit(commitSha);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDTO, result.get(0));
        verify(repository, times(1)).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateByCallstackForRepositoryCallsRepositoryWithRepositoryNameTest() {
        String repoName = "commons-lang";
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedDto = new AggregatedRuntimeCallTreeMeasurementDTO();
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregatedDto);
        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstackForRepository(repoName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDto, result.get(0));
        verify(repository, times(1)).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateByCallstackWithFilterCallsRepositoryWithFilterTest() {
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byCommitSha("def456");
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedDto = new AggregatedRuntimeCallTreeMeasurementDTO();
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregatedDto);
        when(repository.aggregateByCallstack(filter)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstack(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDto, result.get(0));
        verify(repository, times(1)).aggregateByCallstack(filter);
    }

    @Test
    void aggregateByCallStackReturnsMultipleAggregationsTest() {
        AggregatedRuntimeCallTreeMeasurementDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementDTO();
        AggregatedRuntimeCallTreeMeasurementDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementDTO();
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(repository.aggregateByCallstack()).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackReturnsEmptyListWhenNoResultsTest() {
        when(repository.aggregateByCallstack()).thenReturn(Collections.emptyList());

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).aggregateByCallstack();
    }
}
