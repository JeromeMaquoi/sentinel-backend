package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.TotalMethodMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TotalMethodMeasurementRepository extends MongoRepository<TotalMethodMeasurementEntity, String> {
}
