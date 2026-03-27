package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.TotalMethodMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalMethodMeasurementRepository extends MongoRepository<TotalMethodMeasurementEntity, String> {
}
