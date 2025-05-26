package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.AstElemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstElemEntityRepository extends MongoRepository<AstElemEntity, String> {
}
