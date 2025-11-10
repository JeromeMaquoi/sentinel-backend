package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.RuntimeCallTreeMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuntimeCallTreeMeasurementServiceImpl implements MeasurementService<RuntimeCallTreeMeasurementEntityDTO> {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeMeasurementServiceImpl.class);
    private final RuntimeCallTreeMeasurementRepository repository;
    private final RuntimeCallTreeMeasurementEntityMapper mapper;

    public RuntimeCallTreeMeasurementServiceImpl(RuntimeCallTreeMeasurementRepository repository, RuntimeCallTreeMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RuntimeCallTreeMeasurementEntityDTO save(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to save CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        RuntimeCallTreeMeasurementEntity entity = mapper.toEntity(runtimeCallTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public List<RuntimeCallTreeMeasurementEntityDTO> bulkAdd(List<RuntimeCallTreeMeasurementEntityDTO> runtimeCallTreeMeasurementEntityDTOList) {
        List<RuntimeCallTreeMeasurementEntity> entities = mapper.toEntity(runtimeCallTreeMeasurementEntityDTOList);
        entities = repository.insert(entities);
        log.info("{} size CallTreeMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public RuntimeCallTreeMeasurementEntityDTO update(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to update CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        RuntimeCallTreeMeasurementEntity entity = mapper.toEntity(runtimeCallTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<RuntimeCallTreeMeasurementEntityDTO> partialUpdate(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to partial update CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        return repository
            .findById(runtimeCallTreeMeasurementEntityDTO.getId())
            .map(existingEntity -> {
                mapper.partialUpdate(existingEntity, runtimeCallTreeMeasurementEntityDTO);
                return existingEntity;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    public List<RuntimeCallTreeMeasurementEntityDTO> findAll() {
        log.debug("Request to get all CallTreeMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<RuntimeCallTreeMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get CallTreeMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CallTreeMeasurementEntity : {}", id);
        repository.deleteById(id);
    }
}
