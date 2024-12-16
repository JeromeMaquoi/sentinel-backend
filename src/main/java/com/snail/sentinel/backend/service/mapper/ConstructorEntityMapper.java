package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConstructorEntity} and its DTO {@link ConstructorEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConstructorEntityMapper extends EntityMapper<ConstructorEntityDTO, ConstructorEntity> {}
