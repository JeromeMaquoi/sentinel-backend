package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;

public interface ConstructorContextEntityService {
    ConstructorContextEntityDTO save(ConstructorContextDTO constructorContextEntityDTO);
}
