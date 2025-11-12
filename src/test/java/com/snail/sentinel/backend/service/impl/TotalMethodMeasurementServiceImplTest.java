package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.TotalMethodMeasurementEntity;
import com.snail.sentinel.backend.repository.TotalMethodMeasurementRepository;
import com.snail.sentinel.backend.service.dto.TotalMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.TotalMethodMeasurementEntityMapper;
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

class TotalMethodMeasurementServiceImplTest {
    @Mock
    private TotalMethodMeasurementRepository repository;

    @Mock
    private TotalMethodMeasurementEntityMapper mapper;

    @InjectMocks
    private TotalMethodMeasurementServiceImpl service;

    private TotalMethodMeasurementEntity entity;
    private TotalMethodMeasurementEntityDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new TotalMethodMeasurementEntity();
        entity.setId("id1");
        dto = new TotalMethodMeasurementEntityDTO();
        dto.setId("id1");
    }

    @Test
    void saveTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        TotalMethodMeasurementEntityDTO result = service.save(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void bulkAddTest() {
        List<TotalMethodMeasurementEntityDTO> dtoList = List.of(dto);
        List<TotalMethodMeasurementEntity> entityList = List.of(entity);

        when(mapper.toEntity(dtoList)).thenReturn(entityList);
        when(repository.insert(entityList)).thenReturn(entityList);
        when(mapper.toDto(entityList)).thenReturn(dtoList);

        List<TotalMethodMeasurementEntityDTO> result = service.bulkAdd(dtoList);

        assertEquals(dtoList, result);
        verify(repository).insert(entityList);
    }

    @Test
    void updateTest() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        TotalMethodMeasurementEntityDTO result = service.update(dto);

        assertEquals(dto, result);
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        doAnswer(invocation -> {
            TotalMethodMeasurementEntity existingEntity = invocation.getArgument(0);
            TotalMethodMeasurementEntityDTO dtoArg = invocation.getArgument(1);
            existingEntity.setId(dtoArg.getId());
            // Simulate partial update logic
            return null;
        }).when(mapper).partialUpdate(entity, dto);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<TotalMethodMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(repository).findById("id1");
        verify(repository).save(entity);
    }

    @Test
    void partialUpdateNotFoundTest() {
        when(repository.findById("id1")).thenReturn(Optional.empty());

        Optional<TotalMethodMeasurementEntityDTO> result = service.partialUpdate(dto);

        assertTrue(result.isEmpty());
        verify(repository).findById("id1");
        verify(repository, never()).save(any());
    }

    @Test
    void findAllTest() {
        List<TotalMethodMeasurementEntity> entityList = List.of(entity);

        when(repository.findAll()).thenReturn(entityList);
        when(mapper.toDto(entity)).thenReturn(dto);

        List<TotalMethodMeasurementEntityDTO> result = service.findAll();

        assertThat(result).containsExactly(dto);
        verify(repository).findAll();
    }

    @Test
    void findOneTest() {
        when(repository.findById("id1")).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        Optional<TotalMethodMeasurementEntityDTO> result = service.findOne("id1");

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(repository).findById("id1");
    }

    @Test
    void deleteTest() {
        doNothing().when(repository).deleteById("id1");
        service.delete("id1");
        verify(repository).deleteById("id1");
    }
}
