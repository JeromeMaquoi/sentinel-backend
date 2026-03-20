package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.RunIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationRuntimeMeasurementsDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
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
    void bulkAddEmptyListTest() {
        List<RuntimeCallTreeMeasurementEntityDTO> emptyList = Collections.emptyList();
        when(mapper.toEntity(emptyList)).thenReturn(Collections.emptyList());
        when(repository.insert(Collections.emptyList())).thenReturn(Collections.emptyList());
        when(mapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<RuntimeCallTreeMeasurementEntityDTO> result = service.bulkAdd(emptyList);

        assertThat(result).isEmpty();
        verify(repository).insert(Collections.emptyList());
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
    void findAllEmptyTest() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<RuntimeCallTreeMeasurementEntityDTO> result = service.findAll();

        assertThat(result).isEmpty();
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
    void findOneNotFoundTest() {
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());

        Optional<RuntimeCallTreeMeasurementEntityDTO> result = service.findOne("nonexistent");

        assertThat(result).isEmpty();
        verify(repository).findById("nonexistent");
    }

    @Test
    void deleteTest() {
        doNothing().when(repository).deleteById("id1");
        service.delete("id1");
        verify(repository).deleteById("id1");
    }

    @Test
    void aggregateByCallstackCallsRepositoryMethodTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregatedDTO = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(aggregatedDTO);
        when(repository.aggregateByCallstack()).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDTO, result.get(0));
        verify(repository, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackForCommitCallsRepositoryWithCommitShaTest() {
        String commitSha = "123abc123";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregatedDTO = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(aggregatedDTO);
        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForCommit(commitSha);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDTO, result.get(0));
        verify(repository, times(1)).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateByCallstackForRepositoryCallsRepositoryWithRepositoryNameTest() {
        String repoName = "commons-lang";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregatedDto = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(aggregatedDto);
        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForRepository(repoName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDto, result.get(0));
        verify(repository, times(1)).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateByCallstackWithFilterCallsRepositoryWithFilterTest() {
        MeasurementAggregationFilter filter = MeasurementAggregationFilter.byCommitSha("def456");
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregatedDto = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(aggregatedDto);
        when(repository.aggregateByCallstack(filter)).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(aggregatedDto, result.get(0));
        verify(repository, times(1)).aggregateByCallstack(filter);
    }

    @Test
    void aggregateByCallStackReturnsMultipleAggregationsTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(repository.aggregateByCallstack()).thenReturn(expectedResult);

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackReturnsEmptyListWhenNoResultsTest() {
        when(repository.aggregateByCallstack()).thenReturn(Collections.emptyList());

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0, 2.0));
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(1.5, 2.5));
        measurement1.setCallstack(Arrays.asList("method1", "method2"));
        measurement2.setCallstack(Arrays.asList("method1", "method2"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        AggregatedRuntimeCallTreeMeasurementDTO aggregated = result.get(0);
        assertEquals(2, aggregated.getMeasurements().size());
        assertEquals(3.0, aggregated.getMeasurements().get(0).getTotalEnergy());
        assertEquals(4.0, aggregated.getMeasurements().get(1).getTotalEnergy());

        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackForCommitTest() {
        String commitSha = "abc123";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(0.5));
        measurement1.setCallstack(Arrays.asList("testMethod"));

        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(Collections.singletonList(measurement1));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForCommit(commitSha);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0.5, result.get(0).getMeasurements().get(0).getTotalEnergy());

        verify(repository).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryTest() {
        String repoName = "spring-boot";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(2.0, 3.0));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5.0, result.get(0).getMeasurements().get(0).getTotalEnergy());

        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateAcrossIterationsEmptyResultTest() {
        when(repository.aggregateByCallstack()).thenReturn(Collections.emptyList());

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsMultipleCallstacksTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method2"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsHandlesNullValuesTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, null);
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstack()).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        IterationRuntimeMeasurementsDTO iterationMeasurement = result.get(0).getMeasurements().get(0);
        assertThat(iterationMeasurement.getRuntimeValues()).isEmpty();
        assertEquals(0.0, iterationMeasurement.getTotalEnergy());

        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsHandlesNullTimestampsTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0, 2.0));
        measurement.setTimestamps(null);
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstack()).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        IterationRuntimeMeasurementsDTO iterationMeasurement = result.get(0).getMeasurements().get(0);
        assertThat(iterationMeasurement.getTimestamps()).isEmpty();

        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsPreservesIterationDataTest() {
        RunIterationDTO iteration1 = new RunIterationDTO();
        iteration1.setPid(111);
        iteration1.setStartTimestamp(1000L);

        RunIterationDTO iteration2 = new RunIterationDTO();
        iteration2.setPid(222);
        iteration2.setStartTimestamp(2000L);

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setIteration(iteration1);
        measurement1.setCallstack(Arrays.asList("method"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setIteration(iteration2);
        measurement2.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        List<IterationRuntimeMeasurementsDTO> measurements = result.get(0).getMeasurements();
        assertEquals(2, measurements.size());
        assertEquals(111, measurements.get(0).getIteration().getPid());
        assertEquals(222, measurements.get(1).getIteration().getPid());
    }

    @Test
    void aggregateAcrossIterationsPreservesCommitDataTest() {
        RepositorySimpleDTO repo = new RepositorySimpleDTO();
        repo.setName("test-repo");
        repo.setOwner("test-owner");

        CommitSimpleDTO commit = new CommitSimpleDTO();
        commit.setSha("abc123");
        commit.setRepository(repo);

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCommit(commit);
        measurement1.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstack()).thenReturn(Collections.singletonList(measurement1));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack();

        assertNotNull(result);
        assertEquals(1, result.size());
        CommitSimpleDTO resultCommit = result.get(0).getCommit();
        assertEquals("abc123", resultCommit.getSha());
        assertEquals("test-repo", resultCommit.getRepository().getName());
    }


    @Test
    void aggregateAcrossIterationsByCallstackWithMinIterationsNullTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method1"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack(null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackWithMinIterationsOneTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        when(repository.aggregateByCallstack()).thenReturn(Collections.singletonList(measurement1));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackWithMinIterationsTwoTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method1"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack(2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackWithMinIterationsFiltersOutTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method2"));

        when(repository.aggregateByCallstack()).thenReturn(Arrays.asList(measurement1, measurement2));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack(5);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackWithMinIterationsZeroTest() {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstack()).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstack(0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).aggregateByCallstack();
    }

    @Test
    void aggregateAcrossIterationsByCallstackForCommitWithMinIterationsNullTest() {
        String commitSha = "abc123";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForCommit(commitSha, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForCommitWithMinIterationsOneTest() {
        String commitSha = "abc123";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(0.5));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForCommit(commitSha, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForCommitWithMinIterationsThreeTest() {
        String commitSha = "abc123";

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement3 = createMeasurementByIteration("iter3", 3000L, Arrays.asList(3.0));
        measurement3.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(Arrays.asList(measurement1, measurement2, measurement3));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForCommit(commitSha, 3);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForCommitWithMinIterationsFiltersTest() {
        String commitSha = "abc123";

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement1 = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement1.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement2 = createMeasurementByIteration("iter2", 2000L, Arrays.asList(2.0));
        measurement2.setCallstack(Arrays.asList("method1"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement3 = createMeasurementByIteration("iter3", 3000L, Arrays.asList(3.0));
        measurement3.setCallstack(Arrays.asList("method2"));

        when(repository.aggregateByCallstackAndCommitSha(commitSha)).thenReturn(Arrays.asList(measurement1, measurement2, measurement3));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForCommit(commitSha, 2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstackAndCommitSha(commitSha);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryWithMinIterationsNullTest() {
        String repoName = "test-repo";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryWithMinIterationsOneTest() {
        String repoName = "test-repo";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.5));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getMeasurements().size());
        assertEquals(1.5, result.get(0).getMeasurements().get(0).getTotalEnergy());
        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryWithMinIterationsFiveTest() {
        String repoName = "test-repo";

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> measurements = new java.util.ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter" + i, i * 1000L, Arrays.asList((double) i));
            measurement.setCallstack(Arrays.asList("method1"));
            measurements.add(measurement);
        }

        for (int i = 1; i <= 2; i++) {
            AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter" + (5 + i), (5 + i) * 1000L, Arrays.asList((double) (5 + i)));
            measurement.setCallstack(Arrays.asList("method2"));
            measurements.add(measurement);
        }

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(measurements);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName, 5);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryWithMinIterationsHighTest() {
        String repoName = "test-repo";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter1", 1000L, Arrays.asList(1.0));
        measurement.setCallstack(Arrays.asList("method"));

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(Collections.singletonList(measurement));

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    @Test
    void aggregateAcrossIterationsByCallstackForRepositoryWithMinIterationsMultipleCallstacksTest() {
        String repoName = "test-repo";

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> measurements = new java.util.ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter" + i, i * 1000L, Arrays.asList((double) i));
            measurement.setCallstack(Arrays.asList("method1"));
            measurements.add(measurement);
        }

        for (int i = 1; i <= 2; i++) {
            AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = createMeasurementByIteration("iter" + (3 + i), (3 + i) * 1000L, Arrays.asList((double) (3 + i)));
            measurement.setCallstack(Arrays.asList("method2"));
            measurements.add(measurement);
        }

        when(repository.aggregateByCallstackAndRepositoryName(repoName)).thenReturn(measurements);

        List<AggregatedRuntimeCallTreeMeasurementDTO> result = service.aggregateAcrossIterationsByCallstackForRepository(repoName, 3);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getMeasurements().size());
        verify(repository).aggregateByCallstackAndRepositoryName(repoName);
    }

    private AggregatedRuntimeCallTreeMeasurementByIterationDTO createMeasurementByIteration(String iterationId, long timestamp, List<Double> values) {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();

        RunIterationDTO iteration = new RunIterationDTO();
        iteration.setPid(Integer.parseInt(iterationId.replaceAll("[^0-9]", "0")));
        iteration.setStartTimestamp(timestamp);
        measurement.setIteration(iteration);

        measurement.setValues(values);

        List<Long> timestamps = new java.util.ArrayList<>();
        if (values != null) {
            for (int i = 0; i < values.size(); i++) {
                timestamps.add(timestamp + (i * 1000));
            }
        }
        measurement.setTimestamps(timestamps);

        measurement.setScope("APP");
        measurement.setType("runtime_calltree");

        return measurement;
    }
}

