package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.TotalCallTreeMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalCallTreeMeasurementRepository extends MongoRepository<TotalCallTreeMeasurementEntity, String> {
}
