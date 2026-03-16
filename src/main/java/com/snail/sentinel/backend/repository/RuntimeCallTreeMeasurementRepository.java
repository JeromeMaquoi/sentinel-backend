package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RuntimeCallTreeMeasurementRepository extends MongoRepository<RuntimeCallTreeMeasurementEntity, String> {
    List<RuntimeCallTreeMeasurementEntity> findByCommitSha(String commitSha);
}
