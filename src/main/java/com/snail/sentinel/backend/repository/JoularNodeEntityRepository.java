package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for the JoularNodeEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JoularNodeEntityRepository extends MongoRepository<JoularNodeEntity, String> {}
