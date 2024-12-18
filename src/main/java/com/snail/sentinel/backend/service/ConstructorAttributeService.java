package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.dto.RegisterAttributeRequest;

@Deprecated
public interface ConstructorAttributeService {
    ConstructorEntityDTO registerAttribute(RegisterAttributeRequest request);
}
