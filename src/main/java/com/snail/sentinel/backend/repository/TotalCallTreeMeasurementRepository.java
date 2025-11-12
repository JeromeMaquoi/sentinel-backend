package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.TotalCallTreeMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TotalCallTreeMeasurementRepository extends MongoRepository<TotalCallTreeMeasurementEntity, String> {
}
