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
class RuntimeCallTreeMeasurementEntityResourceAggregationIT {
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
        when(service.aggregateAcrossIterationsByCallstack(null)).thenReturn(expectedResult);

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

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(null);
    }

    @Test
    void aggregateByCallstackEmptyResultTest() throws Exception {
        when(service.aggregateAcrossIterationsByCallstack(null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(null);
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
        when(service.aggregateAcrossIterationsByCallstack(null)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[1].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(null);
    }

    @Test
    void aggregateByCallstackForCommitSuccessTest() throws Exception {
        String commitSha = "abc123def456";
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha, null)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[0].measurements", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha, null);
    }

    @Test
    void aggregateByCallstackForCommitNotFoundTest() throws Exception {
        String commitSha = "nonexistent";
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha, null);
    }

    @Test
    void aggregateByCallstackForRepositorySuccessTest() throws Exception {
        String repositoryName = "commons-lang";
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, null)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, null);
    }

    @Test
    void aggregateByCallstackForRepositoryNotFoundTest() throws Exception {
        String repositoryName = "nonexistent-repo";
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, null);
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
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, null)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, null);
    }

    @Test
    void aggregateByCallstackWithMinIterationsTest() throws Exception {
        Integer minIterations = 1;
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstack(minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"))
            .andExpect(jsonPath("$[0].measurements", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(minIterations);
    }

    @Test
    void aggregateByCallstackWithHighMinIterationsFiltersResultsTest() throws Exception {
        Integer minIterations = 5;
        AggregatedRuntimeCallTreeMeasurementDTO aggregatedWith5Iterations = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregatedWith5Iterations.setType("runtime_calltree");
        aggregatedWith5Iterations.setScope("APP");
        aggregatedWith5Iterations.setCallstack(Arrays.asList("method1"));
        List<IterationRuntimeMeasurementsDTO> fiveMeasurements = Arrays.asList(
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO()
        );
        aggregatedWith5Iterations.setMeasurements(fiveMeasurements);

        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregatedWith5Iterations);
        when(service.aggregateAcrossIterationsByCallstack(minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].measurements", hasSize(5)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(minIterations);
    }

    @Test
    void aggregateByCallstackWithMinIterationsReturnsEmptyWhenAllFilteredTest() throws Exception {
        Integer minIterations = 10;
        when(service.aggregateAcrossIterationsByCallstack(minIterations)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(minIterations);
    }

    @Test
    void aggregateByCallstackForCommitWithMinIterationsTest() throws Exception {
        String commitSha = "abc123def456";
        Integer minIterations = 2;
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].measurements", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations);
    }

    @Test
    void aggregateByCallstackForCommitWithMinIterationsFiltersTest() throws Exception {
        String commitSha = "abc123def456";
        Integer minIterations = 3;
        AggregatedRuntimeCallTreeMeasurementDTO aggregated = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated.setType("runtime_calltree");
        aggregated.setScope("APP");
        List<IterationRuntimeMeasurementsDTO> threeMeasurements = Arrays.asList(
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO()
        );
        aggregated.setMeasurements(threeMeasurements);

        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(aggregated);
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].measurements", hasSize(3)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations);
    }

    @Test
    void aggregateByCallstackForCommitWithMinIterationsReturnsEmptyTest() throws Exception {
        String commitSha = "abc123def456";
        Integer minIterations = 20;
        when(service.aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/commit/" + commitSha)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations);
    }

    @Test
    void aggregateByCallstackForRepositoryWithMinIterationsTest() throws Exception {
        String repositoryName = "commons-lang";
        Integer minIterations = 1;
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].type").value("runtime_calltree"));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations);
    }

    @Test
    void aggregateByCallstackForRepositoryWithMinIterationsMultipleResultsTest() throws Exception {
        String repositoryName = "spring-framework";
        Integer minIterations = 2;

        AggregatedRuntimeCallTreeMeasurementDTO aggregated1 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated1.setType("runtime_calltree");
        aggregated1.setScope("APP");
        aggregated1.setCallstack(Arrays.asList("method1"));
        List<IterationRuntimeMeasurementsDTO> twoMeasurements = Arrays.asList(
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO()
        );
        aggregated1.setMeasurements(twoMeasurements);

        AggregatedRuntimeCallTreeMeasurementDTO aggregated2 = new AggregatedRuntimeCallTreeMeasurementDTO();
        aggregated2.setType("runtime_calltree");
        aggregated2.setScope("APP");
        aggregated2.setCallstack(Arrays.asList("method2"));
        List<IterationRuntimeMeasurementsDTO> threeMeasurements = Arrays.asList(
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO(),
            new IterationRuntimeMeasurementsDTO()
        );
        aggregated2.setMeasurements(threeMeasurements);

        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Arrays.asList(aggregated1, aggregated2);
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].measurements", hasSize(2)))
            .andExpect(jsonPath("$[1].measurements", hasSize(3)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations);
    }

    @Test
    void aggregateByCallstackForRepositoryWithMinIterationsReturnsEmptyTest() throws Exception {
        String repositoryName = "nonexistent-repo";
        Integer minIterations = 5;
        when(service.aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/aggregate/repository/" + repositoryName)
                .param("minIterations", minIterations.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstackForRepository(repositoryName, minIterations);
    }

    @Test
    void aggregateByCallstackWithMinIterationsOneTest() throws Exception {
        Integer minIterations = 1;
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstack(minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .param("minIterations", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(minIterations);
    }

    @Test
    void aggregateByCallstackWithMinIterationsZeroTest() throws Exception {
        Integer minIterations = 0;
        List<AggregatedRuntimeCallTreeMeasurementDTO> expectedResult = Collections.singletonList(sampleAggregatedDto);
        when(service.aggregateAcrossIterationsByCallstack(minIterations)).thenReturn(expectedResult);

        mockMvc.perform(get(BASE_URL + "/aggregate")
                .param("minIterations", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).aggregateAcrossIterationsByCallstack(minIterations);
    }
}




