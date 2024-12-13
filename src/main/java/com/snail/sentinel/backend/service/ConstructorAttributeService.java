package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;

public interface ConstructorAttributeService {
    ConstructorEntityDTO registerAttribute(String constructorSignature, String attributeName, String attributeType);
}
