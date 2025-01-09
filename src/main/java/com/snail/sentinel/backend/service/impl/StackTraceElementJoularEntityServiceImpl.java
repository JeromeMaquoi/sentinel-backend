package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.StackTraceElementJoularEntity;
import com.snail.sentinel.backend.repository.StackTraceElementJoularEntityRepository;
import com.snail.sentinel.backend.service.StackTraceElementJoularEntityService;
import com.snail.sentinel.backend.service.dto.StackTraceElementJoularEntityDTO;
import com.snail.sentinel.backend.service.mapper.StackTraceElementJoularEntityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.snail.sentinel.backend.domain.StackTraceElementJoularEntity}.
 */
@Service
public class StackTraceElementJoularEntityServiceImpl implements StackTraceElementJoularEntityService {

    private static final Logger LOG = LoggerFactory.getLogger(StackTraceElementJoularEntityServiceImpl.class);

    private final StackTraceElementJoularEntityRepository stackTraceElementJoularEntityRepository;

    private final StackTraceElementJoularEntityMapper stackTraceElementJoularEntityMapper;

    public StackTraceElementJoularEntityServiceImpl(
        StackTraceElementJoularEntityRepository stackTraceElementJoularEntityRepository,
        StackTraceElementJoularEntityMapper stackTraceElementJoularEntityMapper
    ) {
        this.stackTraceElementJoularEntityRepository = stackTraceElementJoularEntityRepository;
        this.stackTraceElementJoularEntityMapper = stackTraceElementJoularEntityMapper;
    }

    @Override
    public StackTraceElementJoularEntityDTO save(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO) {
        LOG.debug("Request to save StackTraceElementJoularEntity : {}", stackTraceElementJoularEntityDTO);
        StackTraceElementJoularEntity stackTraceElementJoularEntity = stackTraceElementJoularEntityMapper.toEntity(
            stackTraceElementJoularEntityDTO
        );
        stackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);
        return stackTraceElementJoularEntityMapper.toDto(stackTraceElementJoularEntity);
    }

    @Override
    public StackTraceElementJoularEntityDTO update(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO) {
        LOG.debug("Request to update StackTraceElementJoularEntity : {}", stackTraceElementJoularEntityDTO);
        StackTraceElementJoularEntity stackTraceElementJoularEntity = stackTraceElementJoularEntityMapper.toEntity(
            stackTraceElementJoularEntityDTO
        );
        stackTraceElementJoularEntity = stackTraceElementJoularEntityRepository.save(stackTraceElementJoularEntity);
        return stackTraceElementJoularEntityMapper.toDto(stackTraceElementJoularEntity);
    }

    @Override
    public Optional<StackTraceElementJoularEntityDTO> partialUpdate(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO) {
        LOG.debug("Request to partially update StackTraceElementJoularEntity : {}", stackTraceElementJoularEntityDTO);

        return stackTraceElementJoularEntityRepository
            .findById(stackTraceElementJoularEntityDTO.getId())
            .map(existingStackTraceElementJoularEntity -> {
                stackTraceElementJoularEntityMapper.partialUpdate(existingStackTraceElementJoularEntity, stackTraceElementJoularEntityDTO);

                return existingStackTraceElementJoularEntity;
            })
            .map(stackTraceElementJoularEntityRepository::save)
            .map(stackTraceElementJoularEntityMapper::toDto);
    }

    @Override
    public List<StackTraceElementJoularEntityDTO> findAll() {
        LOG.debug("Request to get all StackTraceElementJoularEntities");
        return stackTraceElementJoularEntityRepository
            .findAll()
            .stream()
            .map(stackTraceElementJoularEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<StackTraceElementJoularEntityDTO> findOne(String id) {
        LOG.debug("Request to get StackTraceElementJoularEntity : {}", id);
        return stackTraceElementJoularEntityRepository.findById(id).map(stackTraceElementJoularEntityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete StackTraceElementJoularEntity : {}", id);
        stackTraceElementJoularEntityRepository.deleteById(id);
    }
}
