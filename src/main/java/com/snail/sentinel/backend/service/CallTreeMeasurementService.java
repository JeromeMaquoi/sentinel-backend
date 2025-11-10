package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CallTreeMeasurementEntityEntity;
import com.snail.sentinel.backend.service.dto.CallTreeMeasurementEntityDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing {@link CallTreeMeasurementEntityEntity}.
 */
public interface CallTreeMeasurementService {
    CallTreeMeasurementEntityDTO save(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO);
    List<CallTreeMeasurementEntityDTO> bulkAdd(List<CallTreeMeasurementEntityDTO> callTreeMeasurementEntityDTOList);
    CallTreeMeasurementEntityDTO update(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO);
    Optional<CallTreeMeasurementEntityDTO> partialUpdate(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO);
    List<CallTreeMeasurementEntityDTO> findAll();
    Optional<CallTreeMeasurementEntityDTO> findOne(String id);
    void delete(String id);
}
