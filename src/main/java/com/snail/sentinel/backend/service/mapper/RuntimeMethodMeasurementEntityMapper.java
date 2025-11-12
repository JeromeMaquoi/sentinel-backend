package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.RuntimeMethodMeasurementEntity;
import com.snail.sentinel.backend.service.dto.RuntimeMethodMeasurementEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RuntimeMethodMeasurementEntityMapper extends EntityMapper<RuntimeMethodMeasurementEntityDTO, RuntimeMethodMeasurementEntity> {
}
