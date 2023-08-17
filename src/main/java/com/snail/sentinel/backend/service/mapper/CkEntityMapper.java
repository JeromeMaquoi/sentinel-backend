package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CkEntity} and its DTO {@link CkEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CkEntityMapper extends EntityMapper<CkEntityDTO, CkEntity> {}
