package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.TotalMethodMeasurementEntity;
import com.snail.sentinel.backend.repository.TotalMethodMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.TotalMethodMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TotalMethodMeasurementServiceImpl implements MeasurementService<TotalMethodMeasurementEntityDTO> {
    private static final Logger log = LoggerFactory.getLogger(TotalMethodMeasurementServiceImpl.class);
    private final TotalMethodMeasurementRepository repository;
    private final TotalMethodMeasurementEntityMapper mapper;

    public TotalMethodMeasurementServiceImpl(TotalMethodMeasurementRepository repository, TotalMethodMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TotalMethodMeasurementEntityDTO save(TotalMethodMeasurementEntityDTO measurement) {
        log.debug("Request to save TotalMethodMeasurementEntity : {}", measurement);
        TotalMethodMeasurementEntity measurementEntity = mapper.toEntity(measurement);
        measurementEntity = repository.save(measurementEntity);
        return mapper.toDto(measurementEntity);
    }

    @Override
    public List<TotalMethodMeasurementEntityDTO> bulkAdd(List<TotalMethodMeasurementEntityDTO> measurements) {
        log.debug("Request to bulk Add TotalMethodMeasurementEntity : {}", measurements);
        List<TotalMethodMeasurementEntity> entities = mapper.toEntity(measurements);
        entities = repository.insert(entities);
        log.info("{} size TotalMethodMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public TotalMethodMeasurementEntityDTO update(TotalMethodMeasurementEntityDTO measurement) {
        log.debug("Request to update TotalMethodMeasurementEntity : {}", measurement);
        TotalMethodMeasurementEntity measurementEntity = mapper.toEntity(measurement);
        measurementEntity = repository.save(measurementEntity);
        return mapper.toDto(measurementEntity);
    }

    @Override
    public Optional<TotalMethodMeasurementEntityDTO> partialUpdate(TotalMethodMeasurementEntityDTO measurement) {
        log.debug("Request to partially update TotalMethodMeasurementEntity : {}", measurement);
        return repository
            .findById(measurement.getId())
            .map(existingEntity -> {
                mapper.partialUpdate(existingEntity, measurement);
                return existingEntity;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    public List<TotalMethodMeasurementEntityDTO> findAll() {
        log.debug("Request to get all TotalMethodMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<TotalMethodMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get TotalMethodMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete TotalMethodMeasurementEntity : {}", id);
        repository.deleteById(id);
    }
}
