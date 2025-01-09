package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.StackTraceElementJoularEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StackTraceElementJoularEntity entity.
 */
@Repository
public interface StackTraceElementJoularEntityRepository extends MongoRepository<StackTraceElementJoularEntity, String> {}
