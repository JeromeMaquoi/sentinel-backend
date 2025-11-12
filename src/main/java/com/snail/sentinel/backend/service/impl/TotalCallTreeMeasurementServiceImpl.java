package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.TotalCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.TotalCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.TotalCallTreeMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TotalCallTreeMeasurementServiceImpl implements MeasurementService<TotalCallTreeMeasurementEntityDTO> {
    private static final Logger log = LoggerFactory.getLogger(TotalCallTreeMeasurementServiceImpl.class);
    private final TotalCallTreeMeasurementRepository repository;
    private final TotalCallTreeMeasurementEntityMapper mapper;

    public TotalCallTreeMeasurementServiceImpl(TotalCallTreeMeasurementRepository repository, TotalCallTreeMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TotalCallTreeMeasurementEntityDTO save(TotalCallTreeMeasurementEntityDTO measurement) {
        log.debug("Request to save TotalCallTreeMeasurementEntity : {}", measurement);
        TotalCallTreeMeasurementEntity entity = mapper.toEntity(measurement);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public List<TotalCallTreeMeasurementEntityDTO> bulkAdd(List<TotalCallTreeMeasurementEntityDTO> measurements) {
        log.debug("Request to bulk add TotalCallTreeMeasurementEntity : {}", measurements);
        List<TotalCallTreeMeasurementEntity> entities = mapper.toEntity(measurements);
        entities = repository.insert(entities);
        log.info("{} size TotalCallTreeMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public TotalCallTreeMeasurementEntityDTO update(TotalCallTreeMeasurementEntityDTO measurement) {
        log.debug("Request to update TotalCallTreeMeasurementEntity : {}", measurement);
        TotalCallTreeMeasurementEntity entity = mapper.toEntity(measurement);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<TotalCallTreeMeasurementEntityDTO> partialUpdate(TotalCallTreeMeasurementEntityDTO measurement) {
        log.debug("Request to partially update TotalCallTreeMeasurementEntity : {}", measurement);
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
    public List<TotalCallTreeMeasurementEntityDTO> findAll() {
        log.debug("Request to get all TotalCallTreeMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<TotalCallTreeMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get TotalCallTreeMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete TotalCallTreeMeasurementEntity : {}", id);
        repository.deleteById(id);
    }
}
