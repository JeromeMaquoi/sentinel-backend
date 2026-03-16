package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ConstructorContextDTO;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;

import java.util.List;

public interface ConstructorContextEntityService {
    ConstructorContextEntityDTO save(ConstructorContextDTO constructorContextEntityDTO);
    void saveBatch(List<ConstructorContextDTO> constructorContextEntityDTOS);
}
