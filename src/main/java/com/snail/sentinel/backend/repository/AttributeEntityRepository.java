package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.AttributeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AttributeEntity entity.
 */
@Repository
public interface AttributeEntityRepository extends MongoRepository<AttributeEntity, String> {}
