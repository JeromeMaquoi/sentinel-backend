package com.snail.sentinel.backend.service.impl;

import com.mongodb.DuplicateKeyException;
import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.ConstructorContextEntityService;
import com.snail.sentinel.backend.service.StackTraceEnrichmentService;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.exceptions.ConstructorContextEntityExistsException;
import com.snail.sentinel.backend.service.mapper.ConstructorContextEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConstructorContextEntityServiceImpl implements ConstructorContextEntityService {
    private static final Logger log = LoggerFactory.getLogger(ConstructorContextEntityServiceImpl.class);

    private final ConstructorContextEntityRepository repository;

    private final ConstructorContextEntityMapper mapper;

    public ConstructorContextEntityServiceImpl(ConstructorContextEntityRepository repository, @Qualifier("constructorContextEntityMapperImpl") ConstructorContextEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ConstructorContextEntityDTO save(ConstructorContextDTO dto) {
        log.debug("Saving constructorContextEntityDTO: {}", dto);
        List<ConstructorContextEntityDTO> candidates = repository.findByFileNameAndClassNameAndMethodNameAndParameters(dto.getFileName(), dto.getClassName(), dto.getMethodName(), dto.getParameters());

        ConstructorContextEntityDTO entityDTO = mapper.toDto(dto);
        boolean exists = candidates.stream().anyMatch(entity -> entity.getStacktrace().equals(entityDTO.getStacktrace()));

        if (exists) {
//            log.warn("ConstructorContextEntity already exists for fileName: {}, className: {}, methodName: {}, parameters: {} and stacktrace: {}", dto.getFileName(), dto.getClassName(), dto.getMethodName(), dto.getParameters(), dto.getStacktrace());
            throw new ConstructorContextEntityExistsException("ConstructorContextEntity already exists");
        }

        ConstructorContextEntity constructorContextEntity = mapper.toEntity(entityDTO);
        constructorContextEntity = repository.save(constructorContextEntity);
        return mapper.toDto(constructorContextEntity);
    }

    @Override
    @Transactional
    public void saveBatch(List<ConstructorContextDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            log.warn("Received empty batch of ConstructorContextDTOs");
            return;
        }

        log.debug("Saving batch of constructorContextEntityDTOs: {}", dtos.size());

        List<ConstructorContextEntity> entities = dtos.stream()
            .filter(ConstructorContextDTO::isComplete)
            .map(mapper::toEntity)
            .peek(ConstructorContextEntity::computeStacktraceHash)
            .toList();

        try {
            repository.saveAll(entities);
        } catch (DuplicateKeyException e) {
            log.warn("Duplicate key exception when saving constructorContextEntityDTOs: {}", dtos);
        } catch (Exception e) {
            log.error("Failed to save constructor context batch", e);
            throw e;
        }
    }
}
