package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.TotalMethodMeasurementEntity;
import com.snail.sentinel.backend.service.dto.TotalMethodMeasurementEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalMethodMeasurementEntityMapper extends EntityMapper<TotalMethodMeasurementEntityDTO, TotalMethodMeasurementEntity> {
}
