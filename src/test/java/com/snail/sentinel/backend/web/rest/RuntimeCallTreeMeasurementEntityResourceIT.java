package com.snail.sentinel.backend.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
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

@WebMvcTest(controllers = RuntimeCallTreeMeasurementEntityResource.class)
@AutoConfigureMockMvc(addFilters = false) // disable security filters for MVC slice tests
public class RuntimeCallTreeMeasurementEntityResourceIT {

    private static final String BASE_URL = "/api/v2/measurements/runtime/calltrees";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeasurementService<RuntimeCallTreeMeasurementEntityDTO> service;

    @MockBean
    private RuntimeCallTreeMeasurementRepository repository;

    private RuntimeCallTreeMeasurementEntityDTO sampleDto;

    @BeforeEach
    public void init() {
        sampleDto = new RuntimeCallTreeMeasurementEntityDTO();
        sampleDto.setId("1");
        // set other fields if necessary
    }

    @Test
    public void createCallTreeMeasurementEntity_success() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO toCreate = new RuntimeCallTreeMeasurementEntityDTO();
        // no id for create
        when(service.save(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class))).thenReturn(sampleDto);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", BASE_URL + "/" + sampleDto.getId()))
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).save(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class));
    }

    @Test
    public void createCallTreeMeasurementEntity_withId_badRequest() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO withId = new RuntimeCallTreeMeasurementEntityDTO();
        withId.setId("already");

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withId)))
            .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    public void bulkAddCallTreeMeasurementEntities_success() throws Exception {
        List<RuntimeCallTreeMeasurementEntityDTO> list = Collections.singletonList(sampleDto);
        when(service.bulkAdd(ArgumentMatchers.anyList())).thenReturn(list);

        mockMvc.perform(post(BASE_URL + "/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).bulkAdd(ArgumentMatchers.anyList());
    }

    @Test
    public void updateCallTreeMeasurementEntity_success() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO updated = new RuntimeCallTreeMeasurementEntityDTO();
        updated.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.update(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class))).thenReturn(updated);

        mockMvc.perform(put(BASE_URL + "/" + sampleDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).update(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class));
    }

    @Test
    public void updateCallTreeMeasurementEntity_idMismatch_badRequest() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO updated = new RuntimeCallTreeMeasurementEntityDTO();
        updated.setId("different");

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    public void updateCallTreeMeasurementEntity_notFound_badRequest() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO updated = new RuntimeCallTreeMeasurementEntityDTO();
        updated.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(put(BASE_URL + "/missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isBadRequest());

        verify(service, never()).update(any());
    }

    @Test
    public void partialUpdateCallTreeMeasurementEntity_success() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO patchDto = new RuntimeCallTreeMeasurementEntityDTO();
        patchDto.setId(sampleDto.getId());
        when(repository.existsById(sampleDto.getId())).thenReturn(true);
        when(service.partialUpdate(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class))).thenReturn(Optional.of(patchDto));

        mockMvc.perform(patch(BASE_URL + "/" + sampleDto.getId())
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).partialUpdate(ArgumentMatchers.any(RuntimeCallTreeMeasurementEntityDTO.class));
    }

    @Test
    public void partialUpdateCallTreeMeasurementEntity_notFound_badRequest() throws Exception {
        RuntimeCallTreeMeasurementEntityDTO patchDto = new RuntimeCallTreeMeasurementEntityDTO();
        patchDto.setId("missing");
        when(repository.existsById("missing")).thenReturn(false);

        mockMvc.perform(patch(BASE_URL + "/missing")
                .contentType("application/merge-patch+json")
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isBadRequest());

        verify(service, never()).partialUpdate(any());
    }

    @Test
    public void getAllCallTreeMeasurementEntities_success() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(sampleDto));

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(sampleDto.getId()));

        verify(service, times(1)).findAll();
    }

    @Test
    public void getCallTreeMeasurementEntity_found() throws Exception {
        when(service.findOne(sampleDto.getId())).thenReturn(Optional.of(sampleDto));

        mockMvc.perform(get(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(sampleDto.getId()));

        verify(service, times(1)).findOne(sampleDto.getId());
    }

    @Test
    public void getCallTreeMeasurementEntity_notFound() throws Exception {
        when(service.findOne("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/missing"))
            .andExpect(status().isNotFound());

        verify(service, times(1)).findOne("missing");
    }

    @Test
    public void deleteCallTreeMeasurementEntity_success() throws Exception {
        doNothing().when(service).delete(sampleDto.getId());

        mockMvc.perform(delete(BASE_URL + "/" + sampleDto.getId()))
            .andExpect(status().isNoContent());

        verify(service, times(1)).delete(sampleDto.getId());
    }
}
