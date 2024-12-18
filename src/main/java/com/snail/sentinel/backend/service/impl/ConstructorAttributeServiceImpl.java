package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.AttributeEntityService;
import com.snail.sentinel.backend.service.ConstructorAttributeService;
import com.snail.sentinel.backend.service.ConstructorEntityService;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.dto.RegisterAttributeRequest;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ConstructorAttributeServiceImpl implements ConstructorAttributeService {
    private static final Logger log = LoggerFactory.getLogger(ConstructorAttributeServiceImpl.class);

    private final ConstructorEntityService constructorService;

    private final AttributeEntityService attributeEntityService;

    private final ConstructorEntityMapper constructorEntityMapper;

    private final ConstructorEntityRepository constructorEntityRepository;

    public ConstructorAttributeServiceImpl(ConstructorEntityService constructorService, AttributeEntityService attributeEntityService, ConstructorEntityMapper constructorEntityMapper, ConstructorEntityRepository constructorEntityRepository) {
        this.constructorService = constructorService;
        this.attributeEntityService = attributeEntityService;
        this.constructorEntityMapper = constructorEntityMapper;
        this.constructorEntityRepository = constructorEntityRepository;
    }

    @Override
    @Transactional
    public ConstructorEntityDTO registerAttribute(RegisterAttributeRequest request) {
        String constructorSignature = request.getConstructorSignature();
        ConstructorEntity constructorEntity = constructorService.getOrCreateConstructor(
            request.getConstructorSignature(),
            request.getConstructorName(),
            request.getConstructorFileName(),
            request.getConstructorClassName()
        );

        String attributeName = request.getAttributeName();
        String attributeType = request.getAttributeType();
        if (attributeEntityService.attributeExists(constructorEntity, attributeName, attributeType)) {
            log.debug("Attribute already exists: {} of type {} for constructor {}", attributeName, attributeType, constructorSignature);
            return constructorEntityMapper.toDto(constructorEntity);
        }

        log.info("Creating new attribute for constructor: {} of type {}", constructorSignature, attributeType);

        AttributeEntity attributeEntity = attributeEntityService.createAttribute(attributeName, attributeType);
        constructorEntity.getAttributeEntities().add(attributeEntity);
        ConstructorEntity savedEntity = constructorEntityRepository.save(constructorEntity);

        return constructorEntityMapper.toDto(savedEntity);
    }
}
