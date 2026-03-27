package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ConstructorContextEntityRepository extends MongoRepository<ConstructorContextEntity, String> {
    List<ConstructorContextEntityDTO> findByFileNameAndClassNameAndMethodNameAndParameters(String fileName, String className, String methodName, List<String> parameters);

    /**
     * Find all ConstructorContextEntity documents with the given class name
     * @param className the fully qualified class name
     * @return list of matching constructor context entities
     */
    List<ConstructorContextEntity> findByClassName(String className);

    /**
     * Find all ConstructorContextEntity documents with the given class name and commit SHA
     * @param className the fully qualified class name
     * @param commitSha the commit SHA to filter by
     * @return list of matching constructor context entities
     */
    @Query("{ 'class': ?0, 'commit.sha': ?1 }")
    List<ConstructorContextEntity> findByClassNameAndCommitSha(String className, String commitSha);

    /**
     * Find all ConstructorContextEntity documents with the given class name and repository name
     * @param className the fully qualified class name
     * @param repositoryName the repository name to filter by
     * @return list of matching constructor context entities
     */
    @Query("{ 'class': ?0, 'commit.repository.name': ?1 }")
    List<ConstructorContextEntity> findByClassNameAndCommitRepositoryName(String className, String repositoryName);
}
