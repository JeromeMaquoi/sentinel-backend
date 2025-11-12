package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.repository.RuntimeMethodMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RuntimeMethodMeasurementEntityResource.class)
@AutoConfigureMockMvc(addFilters = false)
class RuntimeMethodMeasurementEntityResourceIT {

    private static final String BASE_URL = "/api/v2/measurements/runtime/methods";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeasurementService<RuntimeMethodMeasurementEntityDTO> service;

    @MockBean
    private RuntimeMethodMeasurementRepository repository;

    private RuntimeMethodMeasurementEntityDTO sampleDto;

    @BeforeEach
    void init() {
        sampleDto = new RuntimeMethodMeasurementEntityDTO();
        sampleDto.setId("1");
    }

    @Test
    void createMethodMeasurementEntity_success() throws Exception {
        RuntimeMethodMeasurementEntityDTO toCreate = new RuntimeMethodMeasurementEntityDTO();
        when(service.save(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", BASE_URL + "/" + sampleDto.getId()))
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).save(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class));
    }

    @Test
    void createMethodMeasurementEntity_withId_badRequest() throws Exception {
        RuntimeMethodMeasurementEntityDTO withId = new RuntimeMethodMeasurementEntityDTO();
        withId.setId("already");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withId)))
            .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    void bulkAddMethodMeasurementEntities_success() throws Exception {
        List<RuntimeMethodMeasurementEntityDTO> list = Collections.singletonList(sampleDto);
        when(service.bulkAdd(ArgumentMatchers.anyList())).thenReturn(list);

        mockMvc.perform(post(BASE_URL + "/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).bulkAdd(ArgumentMatchers.anyList());
    }

    @Test
    void updateMethodMeasurementEntity_success() throws Exception {
        RuntimeMethodMeasurementEntityDTO updated = new RuntimeMethodMeasurementEntityDTO();
        updated.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.update(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class))).thenReturn(updated);

        mockMvc.perform(put(BASE_URL + "/" + sampleDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).update(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class));
    }

    @Test
    void updateMethodMeasurementEntity_idMismatch_badRequest() throws Exception {
        RuntimeMethodMeasurementEntityDTO updated = new RuntimeMethodMeasurementEntityDTO();
        updated.setId("different");

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    void updateMethodMeasurementEntity_notFound_badRequest() throws Exception {
        RuntimeMethodMeasurementEntityDTO updated = new RuntimeMethodMeasurementEntityDTO();
        updated.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(put(BASE_URL + "/missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    void partialUpdateMethodMeasurementEntity_success() throws Exception {
        RuntimeMethodMeasurementEntityDTO patchDto = new RuntimeMethodMeasurementEntityDTO();
        patchDto.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.partialUpdate(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class))).thenReturn(Optional.of(patchDto));

        mockMvc.perform(patch(BASE_URL + "/" + sampleDto.getId())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).partialUpdate(ArgumentMatchers.any(RuntimeMethodMeasurementEntityDTO.class));
    }

    @Test
    void partialUpdateMethodMeasurementEntity_notFound_badRequest() throws Exception {
        RuntimeMethodMeasurementEntityDTO patchDto = new RuntimeMethodMeasurementEntityDTO();
        patchDto.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(patch(BASE_URL + "/missing")
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isBadRequest());

        verify(service, never()).partialUpdate(any());
    }

    @Test
    void getAllMethodMeasurementEntities_success() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(sampleDto));

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).findAll();
    }

    @Test
    void getMethodMeasurementEntity_found() throws Exception {
        when(service.findOne(sampleDto.getId())).thenReturn(Optional.of(sampleDto));

        mockMvc.perform(get(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).findOne(sampleDto.getId());
    }

    @Test
    void getMethodMeasurementEntity_notFound() throws Exception {
        when(service.findOne("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/missing"))
            .andExpect(status().isNotFound());

        verify(service, times(1)).findOne("missing");
    }

    @Test
    void deleteMethodMeasurementEntity_success() throws Exception {
        doNothing().when(service).delete(sampleDto.getId());

        mockMvc.perform(delete(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isNoContent());

        verify(service, times(1)).delete(sampleDto.getId());
    }
}

