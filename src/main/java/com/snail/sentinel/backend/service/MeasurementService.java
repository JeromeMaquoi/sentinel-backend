package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.AbstractEnergyMeasurementEntity;
import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing any type of Measurement Entity.
 */
public interface MeasurementService<D> {
    D save(D measurement);
    List<D> bulkAdd(List<D> measurements);
    D update(D measurement);
    Optional<D> partialUpdate(D measurement);
    List<D> findAll();
    Optional<D> findOne(String id);
    void delete(String id);
}
