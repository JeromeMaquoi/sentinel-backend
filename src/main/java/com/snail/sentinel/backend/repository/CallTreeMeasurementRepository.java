package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.CallTreeMeasurementEntityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CallTreeMeasurementRepository extends MongoRepository<CallTreeMeasurementEntityEntity, String> {
}
