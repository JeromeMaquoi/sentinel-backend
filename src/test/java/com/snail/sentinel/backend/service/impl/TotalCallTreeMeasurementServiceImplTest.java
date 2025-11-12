package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.TotalCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.TotalCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.dto.TotalCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.TotalCallTreeMeasurementEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class TotalCallTreeMeasurementServiceImplTest {
    @Mock
    private TotalCallTreeMeasurementRepository repository;

    @Mock
    private TotalCallTreeMeasurementEntityMapper mapper;

    @InjectMocks
    private TotalCallTreeMeasurementServiceImpl service;

    private TotalCallTreeMeasurementEntity entity;
    private TotalCallTreeMeasurementEntityDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new TotalCallTreeMeasurementEntity();
        entity.setId("id1");
        dto = new TotalCallTreeMeasurementEntityDTO();
        dto.setId("id1");
    }

    @Test
    void saveTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        TotalCallTreeMeasurementEntityDTO result = service.save(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void bulkAddTest() {
        List<TotalCallTreeMeasurementEntityDTO> dtoList = List.of(dto);
        List<TotalCallTreeMeasurementEntity> entityList = List.of(entity);

        when(mapper.toEntity(dtoList)).thenReturn(entityList);
        when(repository.insert(entityList)).thenReturn(entityList);
        when(mapper.toDto(entityList)).thenReturn(dtoList);

        List<TotalCallTreeMeasurementEntityDTO> result = service.bulkAdd(dtoList);

        assertEquals(dtoList, result);
        verify(repository).insert(entityList);
    }

    @Test
    void updateTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        TotalCallTreeMeasurementEntityDTO result = service.update(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            TotalCallTreeMeasurementEntity existingEntity = invocation.getArgument(0);
            TotalCallTreeMeasurementEntityDTO dtoArg = invocation.getArgument(1);
            existingEntity.setId(dtoArg.getId());
            return null;
        }).when(mapper).partialUpdate(entity, dto);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<TotalCallTreeMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isPresent();
        assertEquals(dto, result.orElseThrow());
        verify(repository).findById("id1");
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateNotFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        Optional<TotalCallTreeMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isEmpty();
        verify(repository).findById("id1");
        verify(repository, never()).save(any());
    }

    @Test
    void findAllTest() {
        List<TotalCallTreeMeasurementEntity> entityList = List.of(entity);

        when(repository.findAll()).thenReturn(entityList);
        when(mapper.toDto(entity)).thenReturn(dto);

        List<TotalCallTreeMeasurementEntityDTO> result = service.findAll();

        assertThat(result).containsExactly(dto);
        verify(repository).findAll();
    }

    @Test
    void findOneTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<TotalCallTreeMeasurementEntityDTO> result = service.findOne("id1");

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
}
