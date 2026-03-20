package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeWithConstructorsDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationRuntimeMeasurementsDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RuntimeCallTreeMeasurementEntityResource.class)
class RuntimeCallTreeMeasurementEntityResourceConstructorIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuntimeCallTreeMeasurementService service;

    private AggregatedRuntimeCallTreeMeasurementDTO sampleAggregatedMeasurement;
    private ConstructorContextEntity sampleConstructor;

    @BeforeEach
    void setUp() {
        // Setup sample aggregated measurement
        sampleAggregatedMeasurement = new AggregatedRuntimeCallTreeMeasurementDTO();
        sampleAggregatedMeasurement.setCallstack(List.of(
            "org.apache.commons.lang3.CharRange.<init>",
            "org.apache.commons.lang3.CharRange.is"
        ));
        sampleAggregatedMeasurement.setScope("APP");
        sampleAggregatedMeasurement.setType("runtime_calltree");

        CommitSimpleDTO commit = new CommitSimpleDTO();
        commit.setSha("abc123");
        sampleAggregatedMeasurement.setCommit(commit);

        IterationRuntimeMeasurementsDTO measurement = new IterationRuntimeMeasurementsDTO();
        measurement.setRuntimeValues(List.of(1.5, 2.5));
        measurement.setTotalEnergy(4.0);
        sampleAggregatedMeasurement.setMeasurements(List.of(measurement));

        // Setup sample constructor
        sampleConstructor = new ConstructorContextEntity();
        sampleConstructor.setId("ctor-123");
        sampleConstructor.setClassName("org.apache.commons.lang3.CharRange");
        sampleConstructor.setMethodName("<init>");
        sampleConstructor.setParameters(List.of("char", "char", "boolean"));
    }

    @Test
    void findConstructorsInAggregatedCallstacksWithoutFilter() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacks(null))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].aggregatedMeasurement.callstack", hasSize(2)))
            .andExpect(jsonPath("$[0].aggregatedMeasurement.scope").value("APP"))
            .andExpect(jsonPath("$[0].matchedConstructors", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacks(null);
    }

    @Test
    void findConstructorsInAggregatedCallstacksWithMinIterations() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacks(5))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors?minIterations=5"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].matchedConstructors", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacks(5);
    }

    @Test
    void findConstructorsInAggregatedCallstacksForCommit() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacksForCommit("abc123", null))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors/commit/abc123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].matchedConstructors", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacksForCommit("abc123", null);
    }

    @Test
    void findConstructorsInAggregatedCallstacksForCommitWithMinIterations() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacksForCommit("abc123", 3))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors/commit/abc123?minIterations=3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacksForCommit("abc123", 3);
    }

    @Test
    void findConstructorsInAggregatedCallstacksForRepository() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacksForRepository("commons-lang", null))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors/repository/commons-lang"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].matchedConstructors", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacksForRepository("commons-lang", null);
    }

    @Test
    void findConstructorsInAggregatedCallstacksForRepositoryWithMinIterations() throws Exception {
        Map<String, ConstructorContextEntity> matchedConstructors = new HashMap<>();
        matchedConstructors.put("org.apache.commons.lang3.CharRange#<init>(char,char,boolean)", sampleConstructor);

        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, matchedConstructors);

        when(service.findConstructorsInAggregatedCallstacksForRepository("commons-lang", 2))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors/repository/commons-lang?minIterations=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacksForRepository("commons-lang", 2);
    }

    @Test
    void findConstructorsReturnsEmptyListWhenNoConstructorsFound() throws Exception {
        AggregatedRuntimeCallTreeWithConstructorsDTO response =
            new AggregatedRuntimeCallTreeWithConstructorsDTO(sampleAggregatedMeasurement, Map.of());

        when(service.findConstructorsInAggregatedCallstacks(null))
            .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].matchedConstructors", hasSize(0)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacks(null);
    }

    @Test
    void findConstructorsReturnsEmptyListWhenNoMeasurements() throws Exception {
        when(service.findConstructorsInAggregatedCallstacks(null))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/v2/measurements/runtime/calltrees/constructors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).findConstructorsInAggregatedCallstacks(null);
    }
}





