package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
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
    private AggregatedRuntimeCallTreeMeasurementByIterationDTO sampleAggregatedDto;

    @BeforeEach
    void init() {
        sampleAggregatedDto = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        sampleAggregatedDto.setType("runtime_calltree");
        sampleAggregatedDto.setCallstack(Arrays.asList("method1", "method2", "method3"));
        sampleAggregatedDto.setTimestamps(Arrays.asList(1000L, 2000L, 3000L));
        sampleAggregatedDto.setValues(Arrays.asList(0.5d, 0.7d, 0.9d));
    }

    @Test
    void aggregateByCalstackSuccessTest() throws Exception {
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateByCallstack()).thenReturn(expectedResult);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[0].callstack", hasSize(3)))
            .andExpect(jsonPath("$[0].timestamps", hasSize(3)))
            .andExpect(jsonPath("$[0].values", hasSize(3)));

        verify(service, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackEmptyResultTest() throws Exception {
        when(service.aggregateByCallstack()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackMultipleResultsTest() throws Exception {
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        aggregated1.setType("runtime_calltree");
        aggregated1.setCallstack(Arrays.asList("method1", "method2"));

        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        aggregated2.setType("runtime_calltree");
        aggregated2.setCallstack(Arrays.asList("method3", "method4"));

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(service.aggregateByCallstack()).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[1].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateByCallstack();
    }

    @Test
    void aggregateByCallstackForCommitSuccessTest() throws Exception {
        String commitSha = "abc123def456";
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateByCallstackForCommit(commitSha)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateByCallstackForCommit(commitSha);
    }

    @Test
    void aggregateByCallstackForCommitNotFoundTest() throws Exception {
        String commitSha = "nonexistent";
        when(service.aggregateByCallstackForCommit(commitSha)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateByCallstackForCommit(commitSha);
    }

    @Test
    void aggregateByCallstackForRepositorySuccessTest() throws Exception {
        String repositoryName = "commons-lang";
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateByCallstackForRepository(repositoryName)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateByCallstackForRepository(repositoryName);
    }

    @Test
    void aggregateByCallstackForRepositoryNotFoundTest() throws Exception {
        String repositoryName = "nonexistent-repo";
        when(service.aggregateByCallstackForRepository(repositoryName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateByCallstackForRepository(repositoryName);
    }

    @Test
    void aggregateByCallstackForRepositoryMultipleResultsTest() throws Exception {
        String repositoryName = "spring-framework";
        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        aggregated1.setType("runtime_calltree");

        AggregatedRuntimeCallTreeMeasurementByIterationDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementByIterationDTO();
        aggregated2.setType("runtime_calltree");

        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(service.aggregateByCallstackForRepository(repositoryName)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).aggregateByCallstackForRepository(repositoryName);
    }
}
