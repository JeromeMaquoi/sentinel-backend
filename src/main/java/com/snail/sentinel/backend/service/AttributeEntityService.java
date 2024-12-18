package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.domain.ConstructorEntity;

@Deprecated
public interface AttributeEntityService {
    boolean attributeExists(ConstructorEntity constructorEntity, String attributeName, String attributeType);
    AttributeEntity createAttribute(String attributeName, String attributeType);
}
