package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JoularNodeEntity} and its DTO {@link JoularNodeEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface JoularNodeEntityMapper extends EntityMapper<JoularNodeEntityDTO, JoularNodeEntity> {}
