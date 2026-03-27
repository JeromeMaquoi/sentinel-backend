package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.RuntimeMethodMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuntimeMethodMeasurementRepository extends MongoRepository<RuntimeMethodMeasurementEntity, String> {
}
