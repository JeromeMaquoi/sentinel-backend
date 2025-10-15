package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.repository.ConstructorContextEntityRepository;
import com.snail.sentinel.backend.service.ConstructorContextEntityService;
import com.snail.sentinel.backend.service.StackTraceEnrichmentService;
import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import com.snail.sentinel.backend.service.dto.StackTraceElementDTO;
import com.snail.sentinel.backend.service.exceptions.ConstructorContextEntityExistsException;
import com.snail.sentinel.backend.service.mapper.ConstructorContextEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConstructorContextEntityServiceImpl implements ConstructorContextEntityService {
    private static final Logger log = LoggerFactory.getLogger(ConstructorContextEntityServiceImpl.class);

    private final ConstructorContextEntityRepository repository;

    private final StackTraceEnrichmentService enrichmentService;

    private final ConstructorContextEntityMapper mapper;

    public ConstructorContextEntityServiceImpl(ConstructorContextEntityRepository repository, StackTraceEnrichmentService enrichmentService, @Qualifier("constructorContextEntityMapperImpl") ConstructorContextEntityMapper mapper) {
        this.repository = repository;
        this.enrichmentService = enrichmentService;
        this.mapper = mapper;
    }

    @Override
    public ConstructorContextEntityDTO save(ConstructorContextDTO dto) {
        log.debug("Saving constructorContextEntityDTO: {}", dto);
        Optional<ConstructorContextEntityDTO> existing = repository.findByFileNameAndClassNameAndMethodNameAndParameters(dto.getFileName(), dto.getClassName(), dto.getMethodName(), dto.getParameters());

        if (existing.isPresent()) {
            throw new ConstructorContextEntityExistsException("ConstructorContextEntity already exists");
        }

        List<StackTraceElementDTO> enrichedStacktrace = enrichmentService.enrichStackTrace(dto.getStacktrace());

        ConstructorContextEntityDTO entityDTO = new ConstructorContextEntityDTO(
            dto.getFileName(),
            dto.getClassName(),
            dto.getMethodName(),
            dto.getParameters(),
            dto.getAttributes(),
            enrichedStacktrace,
            dto.getSnapshot()
        );
        ConstructorContextEntity constructorContextEntity = mapper.toEntity(entityDTO);
        constructorContextEntity = repository.save(constructorContextEntity);
        return mapper.toDto(constructorContextEntity);
    }
}
