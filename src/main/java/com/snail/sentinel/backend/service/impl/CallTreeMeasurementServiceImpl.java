package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.CallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.CallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.CallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.CallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.mapper.CallTreeMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallTreeMeasurementServiceImpl implements CallTreeMeasurementService {
    private static final Logger log = LoggerFactory.getLogger(CallTreeMeasurementServiceImpl.class);
    private final CallTreeMeasurementRepository repository;
    private final CallTreeMeasurementEntityMapper mapper;

    public CallTreeMeasurementServiceImpl(CallTreeMeasurementRepository repository, CallTreeMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CallTreeMeasurementEntityDTO save(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO) {
        log.debug("Request to save CallTreeMeasurementEntity : {}", callTreeMeasurementEntityDTO);
        CallTreeMeasurementEntity entity = mapper.toEntity(callTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public List<CallTreeMeasurementEntityDTO> bulkAdd(List<CallTreeMeasurementEntityDTO> callTreeMeasurementEntityDTOList) {
        List<CallTreeMeasurementEntity> entities = mapper.toEntity(callTreeMeasurementEntityDTOList);
        entities = repository.insert(entities);
        log.info("{} size CallTreeMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public CallTreeMeasurementEntityDTO update(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO) {
        log.debug("Request to update CallTreeMeasurementEntity : {}", callTreeMeasurementEntityDTO);
        CallTreeMeasurementEntity entity = mapper.toEntity(callTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<CallTreeMeasurementEntityDTO> partialUpdate(CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO) {
        log.debug("Request to partial update CallTreeMeasurementEntity : {}", callTreeMeasurementEntityDTO);
        return repository
            .findById(callTreeMeasurementEntityDTO.getId())
            .map(existingEntity -> {
                mapper.partialUpdate(existingEntity, callTreeMeasurementEntityDTO);
                return existingEntity;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    public List<CallTreeMeasurementEntityDTO> findAll() {
        log.debug("Request to get all CallTreeMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<CallTreeMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get CallTreeMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CallTreeMeasurementEntity : {}", id);
        repository.deleteById(id);
    }
}
