package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuntimeCallTreeMeasurementRepository extends MongoRepository<RuntimeCallTreeMeasurementEntity, String> {
}
