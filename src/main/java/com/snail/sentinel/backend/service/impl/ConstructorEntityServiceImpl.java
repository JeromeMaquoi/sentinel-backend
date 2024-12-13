package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.ConstructorEntityService;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.snail.sentinel.backend.domain.ConstructorEntity}.
 */
@Service
public class ConstructorEntityServiceImpl implements ConstructorEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(ConstructorEntityServiceImpl.class);

    private final ConstructorEntityRepository constructorEntityRepository;

    private final ConstructorEntityMapper constructorEntityMapper;

    public ConstructorEntityServiceImpl(
        ConstructorEntityRepository constructorEntityRepository,
        ConstructorEntityMapper constructorEntityMapper
    ) {
        this.constructorEntityRepository = constructorEntityRepository;
        this.constructorEntityMapper = constructorEntityMapper;
    }

    @Override
    public ConstructorEntityDTO save(ConstructorEntityDTO constructorEntityDTO) {
        LOG.debug("Request to save ConstructorEntity : {}", constructorEntityDTO);
        ConstructorEntity constructorEntity = constructorEntityMapper.toEntity(constructorEntityDTO);
        constructorEntity = constructorEntityRepository.save(constructorEntity);
        return constructorEntityMapper.toDto(constructorEntity);
    }

    @Override
    public ConstructorEntityDTO update(ConstructorEntityDTO constructorEntityDTO) {
        LOG.debug("Request to update ConstructorEntity : {}", constructorEntityDTO);
        ConstructorEntity constructorEntity = constructorEntityMapper.toEntity(constructorEntityDTO);
        constructorEntity = constructorEntityRepository.save(constructorEntity);
        return constructorEntityMapper.toDto(constructorEntity);
    }

    @Override
    public Optional<ConstructorEntityDTO> partialUpdate(ConstructorEntityDTO constructorEntityDTO) {
        LOG.debug("Request to partially update ConstructorEntity : {}", constructorEntityDTO);

        return constructorEntityRepository
            .findById(constructorEntityDTO.getId())
            .map(existingConstructorEntity -> {
                constructorEntityMapper.partialUpdate(existingConstructorEntity, constructorEntityDTO);

                return existingConstructorEntity;
            })
            .map(constructorEntityRepository::save)
            .map(constructorEntityMapper::toDto);
    }

    @Override
    public List<ConstructorEntityDTO> findAll() {
        LOG.debug("Request to get all ConstructorEntities");
        return constructorEntityRepository
            .findAll()
            .stream()
            .map(constructorEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ConstructorEntityDTO> findOne(String id) {
        LOG.debug("Request to get ConstructorEntity : {}", id);
        return constructorEntityRepository.findById(id).map(constructorEntityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete ConstructorEntity : {}", id);
        constructorEntityRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        LOG.debug("Request to delete all ConstructorEntities");
        constructorEntityRepository.deleteAll();
    }
}
