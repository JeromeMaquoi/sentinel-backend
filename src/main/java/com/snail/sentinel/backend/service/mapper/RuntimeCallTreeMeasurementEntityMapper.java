package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuntimeCallTreeMeasurementEntityMapper extends EntityMapper<RuntimeCallTreeMeasurementEntityDTO, RuntimeCallTreeMeasurementEntity> {
}
