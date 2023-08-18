package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.CommitEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the CommitEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitEntityRepository extends MongoRepository<CommitEntity, String> {
    Optional<CommitEntity> findOneBySha(String sha);
}
