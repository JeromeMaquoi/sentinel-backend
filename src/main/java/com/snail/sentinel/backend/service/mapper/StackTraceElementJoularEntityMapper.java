package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.StackTraceElementJoularEntity;
import com.snail.sentinel.backend.service.dto.StackTraceElementJoularEntityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StackTraceElementJoularEntity} and its DTO {@link StackTraceElementJoularEntityDTO}.
 */
@Mapper(componentModel = "spring")
public interface StackTraceElementJoularEntityMapper
    extends EntityMapper<StackTraceElementJoularEntityDTO, StackTraceElementJoularEntity> {}
