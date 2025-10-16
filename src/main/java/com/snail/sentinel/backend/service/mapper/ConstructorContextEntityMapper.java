package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConstructorContextEntityMapper extends EntityMapper<ConstructorContextEntityDTO, ConstructorContextEntity> {
}
