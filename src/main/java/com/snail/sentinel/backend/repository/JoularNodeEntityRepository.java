package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.JoularNodeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the JoularNodeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoularNodeEntityRepository extends MongoRepository<JoularNodeEntity, String> {
    int countByCommitSha(String commitSha);
    void deleteByCommitSha(String commitSha);
}
