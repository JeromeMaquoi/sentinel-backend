package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeMethodMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeMethodMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.RuntimeMethodMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuntimeMethodMeasurementServiceImpl implements MeasurementService<RuntimeMethodMeasurementEntityDTO> {
    private static final Logger log = LoggerFactory.getLogger(RuntimeMethodMeasurementServiceImpl.class);
    private final RuntimeMethodMeasurementRepository repository;
    private final RuntimeMethodMeasurementEntityMapper mapper;

    public RuntimeMethodMeasurementServiceImpl(RuntimeMethodMeasurementRepository repository, RuntimeMethodMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RuntimeMethodMeasurementEntityDTO save(RuntimeMethodMeasurementEntityDTO measurement) {
        log.debug("Request to save RuntimeMethodMeasurementEntity : {}", measurement);
        RuntimeMethodMeasurementEntity entity = mapper.toEntity(measurement);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public List<RuntimeMethodMeasurementEntityDTO> bulkAdd(List<RuntimeMethodMeasurementEntityDTO> measurements) {
        log.debug("Request to bulkAdd RuntimeMethodMeasurementEntity : {}", measurements);
        List<RuntimeMethodMeasurementEntity> entities = mapper.toEntity(measurements);
        entities = repository.insert(entities);
        log.info("{} size RuntimeMethodMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public RuntimeMethodMeasurementEntityDTO update(RuntimeMethodMeasurementEntityDTO measurement) {
        log.debug("Request to update RuntimeMethodMeasurementEntity : {}", measurement);
        RuntimeMethodMeasurementEntity entity = mapper.toEntity(measurement);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<RuntimeMethodMeasurementEntityDTO> partialUpdate(RuntimeMethodMeasurementEntityDTO measurement) {
        log.debug("Request to partial update RuntimeMethodMeasurementEntity : {}", measurement);
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
    public List<RuntimeMethodMeasurementEntityDTO> findAll() {
        log.debug("Request to get all RuntimeMethodMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<RuntimeMethodMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get RuntimeMethodMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RuntimeMethodMeasurementEntity : {}", id);
        repository.deleteById(id);
    }
}
