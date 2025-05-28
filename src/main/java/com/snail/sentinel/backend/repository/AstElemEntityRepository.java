package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.AstElemEntity;
import com.snail.sentinel.backend.service.dto.AstElemEntityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AstElemEntityRepository extends MongoRepository<AstElemEntity, String> {
    @Query("""
    {
        "file": ?0,
        "class": ?1,
        "method": ?2,
        "$expr": {
            "$and": [
                { "$lte":  ["$line",?3 ]},
                { "$gte":  [ { "$add": ["$line", "$loc" ]}, ?3 ]}
            ]
        }
    }
    """)
    Optional<AstElemEntityDTO> findMatchingAstElem(String fileName, String className, String methodName, int lineNumber);
}
