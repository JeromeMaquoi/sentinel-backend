package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.dto.RegisterAttributeRequest;

public interface ConstructorAttributeService {
    ConstructorEntityDTO registerAttribute(RegisterAttributeRequest request);
}
