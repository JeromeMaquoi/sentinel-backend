package com.snail.sentinel.backend.service.mapper;

import com.snail.sentinel.backend.domain.CallTreeMeasurementEntity;
import com.snail.sentinel.backend.service.dto.CallTreeMeasurementEntityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CallTreeMeasurementEntityMapper extends EntityMapper<CallTreeMeasurementEntityDTO, CallTreeMeasurementEntity> {
}
