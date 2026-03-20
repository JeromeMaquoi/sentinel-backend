package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.ConstructorContextEntity;
import com.snail.sentinel.backend.service.dto.ConstructorContextEntityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConstructorContextEntityRepository extends MongoRepository<ConstructorContextEntity, String> {
    List<ConstructorContextEntityDTO> findByFileNameAndClassNameAndMethodNameAndParameters(String fileName, String className, String methodName, List<String> parameters);

    /**
     * Find all ConstructorContextEntity documents with the given class name
     * @param className the fully qualified class name
     * @return list of matching constructor context entities
     */
    List<ConstructorContextEntity> findByClassName(String className);
}
