package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.TotalCallTreeMeasurementEntity;
import com.snail.sentinel.backend.service.dto.TotalCallTreeMeasurementEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalCallTreeMeasurementEntityMapper extends EntityMapper<TotalCallTreeMeasurementEntityDTO, TotalCallTreeMeasurementEntity> {
}
