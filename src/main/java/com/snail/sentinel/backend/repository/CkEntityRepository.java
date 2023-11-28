package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.CkEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data MongoDB repository for the CkEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CkEntityRepository extends MongoRepository<CkEntity, String> {
    List<CkEntity> findByCommitSha(String sha);
    List<CkEntity> findByCommitShaAndName(String sha, String name);
}
