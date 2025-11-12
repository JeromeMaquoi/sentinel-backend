package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.repository.TotalMethodMeasurementRepository;
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

@WebMvcTest(controllers = TotalMethodMeasurementEntityResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class TotalMethodMeasurementEntityResourceIT {

    private static final String BASE_URL = "/api/v2/measurements/total/methods";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeasurementService<TotalMethodMeasurementEntityDTO> service;

    @MockBean
    private TotalMethodMeasurementRepository repository;

    private TotalMethodMeasurementEntityDTO sampleDto;

    @BeforeEach
    void init() {
        sampleDto = new TotalMethodMeasurementEntityDTO();
        sampleDto.setId("1");
    }

    @Test
    public void createTotalMethodMeasurementEntity_success() throws Exception {
        TotalMethodMeasurementEntityDTO toCreate = new TotalMethodMeasurementEntityDTO();
        when(service.save(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", BASE_URL + "/" + sampleDto.getId()))
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).save(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class));
    }

    @Test
    public void createTotalMethodMeasurementEntity_withId_badRequest() throws Exception {
        TotalMethodMeasurementEntityDTO withId = new TotalMethodMeasurementEntityDTO();
        withId.setId("already");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withId)))
            .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    public void bulkAddTotalMethodMeasurementEntities_success() throws Exception {
        List<TotalMethodMeasurementEntityDTO> list = Collections.singletonList(sampleDto);
        when(service.bulkAdd(ArgumentMatchers.anyList())).thenReturn(list);

        mockMvc.perform(post(BASE_URL + "/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).bulkAdd(ArgumentMatchers.anyList());
    }

    @Test
    public void updateTotalMethodMeasurementEntity_success() throws Exception {
        TotalMethodMeasurementEntityDTO updated = new TotalMethodMeasurementEntityDTO();
        updated.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.update(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class))).thenReturn(updated);

        mockMvc.perform(put(BASE_URL + "/" + sampleDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).update(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class));
    }

    @Test
    public void updateTotalMethodMeasurementEntity_idMismatch_badRequest() throws Exception {
        TotalMethodMeasurementEntityDTO updated = new TotalMethodMeasurementEntityDTO();
        updated.setId("different");

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    public void updateTotalMethodMeasurementEntity_notFound_badRequest() throws Exception {
        TotalMethodMeasurementEntityDTO updated = new TotalMethodMeasurementEntityDTO();
        updated.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(put(BASE_URL + "/missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    public void partialUpdateTotalMethodMeasurementEntity_success() throws Exception {
        TotalMethodMeasurementEntityDTO patchDto = new TotalMethodMeasurementEntityDTO();
        patchDto.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.partialUpdate(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class))).thenReturn(Optional.of(patchDto));

        mockMvc.perform(patch(BASE_URL + "/" + sampleDto.getId())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).partialUpdate(ArgumentMatchers.any(TotalMethodMeasurementEntityDTO.class));
    }

    @Test
    public void partialUpdateTotalMethodMeasurementEntity_notFound_badRequest() throws Exception {
        TotalMethodMeasurementEntityDTO patchDto = new TotalMethodMeasurementEntityDTO();
        patchDto.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(patch(BASE_URL + "/missing")
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isBadRequest());

        verify(service, never()).partialUpdate(any());
    }

    @Test
    public void getAllTotalMethodMeasurementEntities_success() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(sampleDto));

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).findAll();
    }

    @Test
    public void getTotalMethodMeasurementEntity_found() throws Exception {
        when(service.findOne(sampleDto.getId())).thenReturn(Optional.of(sampleDto));

        mockMvc.perform(get(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).findOne(sampleDto.getId());
    }

    @Test
    public void getTotalMethodMeasurementEntity_notFound() throws Exception {
        when(service.findOne("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/missing"))
            .andExpect(status().isNotFound());

        verify(service, times(1)).findOne("missing");
    }

    @Test
    public void deleteTotalMethodMeasurementEntity_success() throws Exception {
        doNothing().when(service).delete(sampleDto.getId());

        mockMvc.perform(delete(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isNoContent());

        verify(service, times(1)).delete(sampleDto.getId());
    }
}

