package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link RuntimeCallTreeMeasurementEntity}.
 */
public interface RuntimeCallTreeMeasurementService {
    RuntimeCallTreeMeasurementEntityDTO save(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO);
    List<RuntimeCallTreeMeasurementEntityDTO> bulkAdd(List<RuntimeCallTreeMeasurementEntityDTO> runtimeCallTreeMeasurementEntityDTOList);
    RuntimeCallTreeMeasurementEntityDTO update(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO);
    Optional<RuntimeCallTreeMeasurementEntityDTO> partialUpdate(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO);
    List<RuntimeCallTreeMeasurementEntityDTO> findAll();
    Optional<RuntimeCallTreeMeasurementEntityDTO> findOne(String id);
    void delete(String id);
}
