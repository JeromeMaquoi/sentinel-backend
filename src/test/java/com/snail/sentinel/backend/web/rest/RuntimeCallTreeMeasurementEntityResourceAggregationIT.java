package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.RunIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationRuntimeMeasurementsDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RuntimeCallTreeMeasurementEntityResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class RuntimeCallTreeMeasurementEntityResourceAggregationIT {
    private static final String BASE_URL = "/api/v2/measurements/runtime/calltrees";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RuntimeCallTreeMeasurementService service;
    @MockBean
    private RuntimeCallTreeMeasurementRepository repository;
    private AggregatedRuntimeCallTreeMeasurementDTO sampleAggregatedDto;

    @BeforeEach
    void init() {
        sampleAggregatedDto = new AggregatedRuntimeCallTreeMeasurementDTO();
        sampleAggregatedDto.setType("runtime_calltree");
        sampleAggregatedDto.setScope("APP");
        sampleAggregatedDto.setCallstack(Arrays.asList("method1", "method2", "method3"));

        // Create commit info
        RepositorySimpleDTO repositoryDTO = new RepositorySimpleDTO();
        repositoryDTO.setName("test-repo");
        repositoryDTO.setOwner("test-owner");

        CommitSimpleDTO commitDTO = new CommitSimpleDTO();
        commitDTO.setSha("abc123");
        commitDTO.setRepository(repositoryDTO);
        sampleAggregatedDto.setCommit(commitDTO);

        // Create an iteration measurement
        IterationRuntimeMeasurementsDTO iterationMeasurement = new IterationRuntimeMeasurementsDTO();

        RunIterationDTO iteration = new RunIterationDTO();
        iteration.setPid(12345);
        iteration.setStartTimestamp(1000L);
        iterationMeasurement.setIteration(iteration);

        iterationMeasurement.setRuntimeValues(Arrays.asList(0.5d, 0.7d, 0.9d));
        iterationMeasurement.setTimestamps(Arrays.asList(1000L, 2000L, 3000L));
        iterationMeasurement.setTotalEnergy(2.1d);

        sampleAggregatedDto.setMeasurements(Collections.singletonList(iterationMeasurement));
    }

    @Test
    void aggregateByCalstackSuccessTest() throws Exception {
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstack()).thenReturn(expectedResult);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[0].scope").value("APP"))
            .andExpect(jsonPath("$[0].callstack", hasSize(3)))
            .andExpect(jsonPath("$[0].measurements", hasSize(1)))
            .andExpect(jsonPath("$[0].measurements[0].runtimeValues", hasSize(3)))
            .andExpect(jsonPath("$[0].measurements[0].timestamps", hasSize(3)))
            .andExpect(jsonPath("$[0].measurements[0].totalEnergy").value(2.1d));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack();
    }

    @Test
    void aggregateByCallstackEmptyResultTest() throws Exception {
        when(service.aggregateAcrossIterationsByCallstack()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack();
    }

    @Test
    void aggregateByCallstackMultipleResultsTest() throws Exception {
        AggregatedRuntimeCallTreeMeasurementDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated1.setType("runtime_calltree");
        aggregated1.setScope("APP");
        aggregated1.setCallstack(Arrays.asList("method1", "method2"));
        aggregated1.setMeasurements(Collections.emptyList());

        AggregatedRuntimeCallTreeMeasurementDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated2.setType("runtime_calltree");
        aggregated2.setScope("APP");
        aggregated2.setCallstack(Arrays.asList("method3", "method4"));
        aggregated2.setMeasurements(Collections.emptyList());

        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(service.aggregateAcrossIterationsByCallstack()).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[1].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack();
    }

    @Test
    void aggregateByCallstackForCommitSuccessTest() throws Exception {
        String commitSha = "abc123def456";
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[0].measurements", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha);
    }

    @Test
    void aggregateByCallstackForCommitNotFoundTest() throws Exception {
        String commitSha = "nonexistent";
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha);
    }

    @Test
    void aggregateByCallstackForRepositorySuccessTest() throws Exception {
        String repositoryName = "commons-lang";
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName);
    }

    @Test
    void aggregateByCallstackForRepositoryNotFoundTest() throws Exception {
        String repositoryName = "nonexistent-repo";
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName);
    }

    @Test
    void aggregateByCallstackForRepositoryMultipleResultsTest() throws Exception {
        String repositoryName = "spring-framework";
        AggregatedRuntimeCallTreeMeasurementDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated1.setType("runtime_calltree");
        aggregated1.setScope("APP");
        aggregated1.setMeasurements(Collections.emptyList());

        AggregatedRuntimeCallTreeMeasurementDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated2.setType("runtime_calltree");
        aggregated2.setScope("APP");
        aggregated2.setMeasurements(Collections.emptyList());

        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName);
    }
}




