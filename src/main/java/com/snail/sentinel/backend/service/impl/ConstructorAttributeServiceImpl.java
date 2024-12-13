package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.ConstructorAttributeService;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.mapper.ConstructorEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ConstructorAttributeServiceImpl implements ConstructorAttributeService {
    private static final Logger log = LoggerFactory.getLogger(ConstructorAttributeServiceImpl.class);

    private final ConstructorEntityRepository constructorEntityRepository;

    private final AttributeEntityRepository attributeEntityRepository;

    private final ConstructorEntityMapper constructorEntityMapper;

    public ConstructorAttributeServiceImpl(ConstructorEntityRepository constructorEntityRepository, AttributeEntityRepository attributeEntityRepository, ConstructorEntityMapper constructorEntityMapper) {
        this.constructorEntityRepository = constructorEntityRepository;
        this.attributeEntityRepository = attributeEntityRepository;
        this.constructorEntityMapper = constructorEntityMapper;
    }

    @Override
    @Transactional
    public ConstructorEntityDTO registerAttribute(String constructorSignature, String attributeName, String attributeType) {
        ConstructorEntity constructorEntity = constructorEntityRepository.findBySignature(constructorSignature).orElseGet(() -> {
            ConstructorEntity newConstructorEntity = new ConstructorEntity();
            newConstructorEntity.setSignature(constructorSignature);
            return constructorEntityRepository.save(newConstructorEntity);
        });

        boolean attributeExists = constructorEntity.getAttributeEntities().stream().anyMatch(attributeEntity -> attributeEntity.getName().equals(attributeName) && attributeEntity.getType().equals(attributeType));
        if (attributeExists) {
            log.info("Attribute already exists: {} of type {} for constructor {}", attributeName, attributeType, constructorSignature);
            return constructorEntityMapper.toDto(constructorEntity);
        }

        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName(attributeName);
        attributeEntity.setType(attributeType);
        attributeEntity = attributeEntityRepository.save(attributeEntity);
        log.info("After attributeEntity save: {}", attributeEntity);

        constructorEntity.getAttributeEntities().add(attributeEntity);
        ConstructorEntity savedEntity = constructorEntityRepository.save(constructorEntity);
        return constructorEntityMapper.toDto(savedEntity);
    }
}
