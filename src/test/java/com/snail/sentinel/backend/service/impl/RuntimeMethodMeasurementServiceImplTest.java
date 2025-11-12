package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeMethodMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeMethodMeasurementRepository;
import com.snail.sentinel.backend.service.dto.RuntimeMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.RuntimeMethodMeasurementEntityMapper;
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

class RuntimeMethodMeasurementServiceImplTest {
    @Mock
    private RuntimeMethodMeasurementRepository repository;

    @Mock
    private RuntimeMethodMeasurementEntityMapper mapper;

    @InjectMocks
    private RuntimeMethodMeasurementServiceImpl service;

    private RuntimeMethodMeasurementEntity entity;
    private RuntimeMethodMeasurementEntityDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new RuntimeMethodMeasurementEntity();
        entity.setId("id1");
        dto = new RuntimeMethodMeasurementEntityDTO();
        dto.setId("id1");
    }

    @Test
    void saveTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RuntimeMethodMeasurementEntityDTO result = service.save(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void bulkAddTest() {
        List<RuntimeMethodMeasurementEntityDTO> dtoList = List.of(dto);
        List<RuntimeMethodMeasurementEntity> entityList = List.of(entity);

        when(mapper.toEntity(dtoList)).thenReturn(entityList);
        when(repository.insert(entityList)).thenReturn(entityList);
        when(mapper.toDto(entityList)).thenReturn(dtoList);

        List<RuntimeMethodMeasurementEntityDTO> result = service.bulkAdd(dtoList);

        assertEquals(dtoList, result);
        verify(repository).insert(entityList);
    }

    @Test
    void updateTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        RuntimeMethodMeasurementEntityDTO result = service.update(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            RuntimeMethodMeasurementEntity existingEntity = invocation.getArgument(0);
            RuntimeMethodMeasurementEntityDTO dtoArg = invocation.getArgument(1);
            existingEntity.setId(dtoArg.getId());
            return null;
        }).when(mapper).partialUpdate(entity, dto);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<RuntimeMethodMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isPresent();
        assertEquals(dto, result.orElseThrow());
        verify(repository).findById("id1");
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateNotFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        Optional<RuntimeMethodMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertThat(result).isEmpty();
        verify(repository).findById("id1");
        verify(repository, never()).save(any());
    }

    @Test
    void findAllTest() {
        List<RuntimeMethodMeasurementEntity> entityList = List.of(entity);

        when(repository.findAll()).thenReturn(entityList);
        when(mapper.toDto(entity)).thenReturn(dto);

        List<RuntimeMethodMeasurementEntityDTO> result = service.findAll();

        assertThat(result).containsExactly(dto);
        verify(repository).findAll();
    }

    @Test
    void findOneTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<RuntimeMethodMeasurementEntityDTO> result = service.findOne("id1");

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
