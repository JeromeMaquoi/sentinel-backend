package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.repository.TotalCallTreeMeasurementRepository;
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

@WebMvcTest(controllers = TotalCallTreeMeasurementEntityResource.class)
@AutoConfigureMockMvc(addFilters = false)
class TotalCallTreeMeasurementEntityResourceIT {

    private static final String BASE_URL = "/api/v2/measurements/total/calltrees";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeasurementService<TotalCallTreeMeasurementEntityDTO> service;

    @MockBean
    private TotalCallTreeMeasurementRepository repository;

    private TotalCallTreeMeasurementEntityDTO sampleDto;

    @BeforeEach
    void init() {
        sampleDto = new TotalCallTreeMeasurementEntityDTO();
        sampleDto.setId("1");
    }

    @Test
    void createTotalCallTreeMeasurementEntity_success() throws Exception {
        TotalCallTreeMeasurementEntityDTO toCreate = new TotalCallTreeMeasurementEntityDTO();
        when(service.save(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", BASE_URL + "/" + sampleDto.getId()))
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).save(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class));
    }

    @Test
    void createTotalCallTreeMeasurementEntity_withId_badRequest() throws Exception {
        TotalCallTreeMeasurementEntityDTO withId = new TotalCallTreeMeasurementEntityDTO();
        withId.setId("already");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withId)))
            .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    void bulkAddTotalCallTreeMeasurementEntities_success() throws Exception {
        List<TotalCallTreeMeasurementEntityDTO> list = Collections.singletonList(sampleDto);
        when(service.bulkAdd(ArgumentMatchers.anyList())).thenReturn(list);

        mockMvc.perform(post(BASE_URL + "/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).bulkAdd(ArgumentMatchers.anyList());
    }

    @Test
    void updateTotalCallTreeMeasurementEntity_success() throws Exception {
        TotalCallTreeMeasurementEntityDTO updated = new TotalCallTreeMeasurementEntityDTO();
        updated.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.update(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class))).thenReturn(updated);

        mockMvc.perform(put(BASE_URL + "/" + sampleDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).update(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class));
    }

    @Test
    void updateTotalCallTreeMeasurementEntity_idMismatch_badRequest() throws Exception {
        TotalCallTreeMeasurementEntityDTO updated = new TotalCallTreeMeasurementEntityDTO();
        updated.setId("different");

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    void updateTotalCallTreeMeasurementEntity_notFound_badRequest() throws Exception {
        TotalCallTreeMeasurementEntityDTO updated = new TotalCallTreeMeasurementEntityDTO();
        updated.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(put(BASE_URL + "/missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    void partialUpdateTotalCallTreeMeasurementEntity_success() throws Exception {
        TotalCallTreeMeasurementEntityDTO patchDto = new TotalCallTreeMeasurementEntityDTO();
        patchDto.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.partialUpdate(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class))).thenReturn(Optional.of(patchDto));

        mockMvc.perform(patch(BASE_URL + "/" + sampleDto.getId())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).partialUpdate(ArgumentMatchers.any(TotalCallTreeMeasurementEntityDTO.class));
    }

    @Test
    void partialUpdateTotalCallTreeMeasurementEntity_notFound_badRequest() throws Exception {
        TotalCallTreeMeasurementEntityDTO patchDto = new TotalCallTreeMeasurementEntityDTO();
        patchDto.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(patch(BASE_URL + "/missing")
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isBadRequest());

        verify(service, never()).partialUpdate(any());
    }

    @Test
    void getAllTotalCallTreeMeasurementEntities_success() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(sampleDto));

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).findAll();
    }

    @Test
    void getTotalCallTreeMeasurementEntity_found() throws Exception {
        when(service.findOne(sampleDto.getId())).thenReturn(Optional.of(sampleDto));

        mockMvc.perform(get(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).findOne(sampleDto.getId());
    }

    @Test
    void getTotalCallTreeMeasurementEntity_notFound() throws Exception {
        when(service.findOne("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/missing"))
            .andExpect(status().isNotFound());

        verify(service, times(1)).findOne("missing");
    }

    @Test
    void deleteTotalCallTreeMeasurementEntity_success() throws Exception {
        doNothing().when(service).delete(sampleDto.getId());

        mockMvc.perform(delete(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isNoContent());

        verify(service, times(1)).delete(sampleDto.getId());
    }
}

