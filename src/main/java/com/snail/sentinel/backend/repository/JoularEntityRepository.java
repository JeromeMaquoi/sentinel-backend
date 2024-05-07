package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.JoularEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoularEntityRepository extends MongoRepository<JoularEntity, String> {
    List<JoularEntity> findByCommitSha(String sha);

    List<JoularEntity> findByCommitShaAndMeasurableElementClassNameAndMeasurableElementClassMethodSignature(String sha, String className, String classMethodSignature);

    void deleteJoularEntitiesByCommit_Repository_Name(String repoName);

    int countByCommitSha(String sha);

    void deleteByCommitSha(String commitSha);
}
