package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.JoularEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoularEntityRepository extends MongoRepository<JoularEntity, String> {}
