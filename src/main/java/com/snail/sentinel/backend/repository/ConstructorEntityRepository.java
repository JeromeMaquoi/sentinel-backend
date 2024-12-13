package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ConstructorEntity entity.
 */
@Repository
public interface ConstructorEntityRepository extends MongoRepository<ConstructorEntity, String> {}
