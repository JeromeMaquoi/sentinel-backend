package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.CallTreeMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CallTreeMeasurementRepository extends MongoRepository<CallTreeMeasurementEntity, String> {
}
