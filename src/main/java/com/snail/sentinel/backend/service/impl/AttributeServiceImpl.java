package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
import com.snail.sentinel.backend.service.AttributeService;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {
    private final AttributeEntityRepository attributeEntityRepository;

    public AttributeServiceImpl(AttributeEntityRepository attributeEntityRepository) {
        this.attributeEntityRepository = attributeEntityRepository;
    }

    @Override
    public boolean attributeExists(ConstructorEntity constructorEntity, String attributeName, String attributeType) {
        return constructorEntity.getAttributeEntities().stream().anyMatch(attributeEntity -> attributeEntity.getName().equals(attributeName) && attributeEntity.getType().equals(attributeType));
    }

    @Override
    public AttributeEntity createAttribute(String attributeName, String attributeType) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setName(attributeName);
        attributeEntity.setType(attributeType);
        return attributeEntityRepository.save(attributeEntity);
    }
}
