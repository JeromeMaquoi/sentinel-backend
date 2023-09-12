package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.CkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the CkEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CkEntityRepository extends MongoRepository<CkEntity, String>, CkEntityRepositoryAggregation {}
