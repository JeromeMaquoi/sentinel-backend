package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.AstElemEntity;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import org.mapstruct.*;

@Mapper(componentModel =  "spring")
public interface AstElemEntityMapper extends EntityMapper<AstElemEntityDTO, AstElemEntity> {
}
