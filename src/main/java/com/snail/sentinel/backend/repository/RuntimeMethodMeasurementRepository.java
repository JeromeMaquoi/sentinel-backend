package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.RuntimeMethodMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuntimeMethodMeasurementRepository extends MongoRepository<RuntimeMethodMeasurementEntity, String> {
}
