package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.ConstructorEntity;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.snail.sentinel.backend.domain.ConstructorEntity}.
 */
public interface ConstructorEntityService {
    /**
     * Save a constructorEntity.
     *
     * @param constructorEntityDTO the entity to save.
     * @return the persisted entity.
     */
    ConstructorEntityDTO save(ConstructorEntityDTO constructorEntityDTO);

    /**
     * Updates a constructorEntity.
     *
     * @param constructorEntityDTO the entity to update.
     * @return the persisted entity.
     */
    ConstructorEntityDTO update(ConstructorEntityDTO constructorEntityDTO);

    /**
     * Partially updates a constructorEntity.
     *
     * @param constructorEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConstructorEntityDTO> partialUpdate(ConstructorEntityDTO constructorEntityDTO);

    /**
     * Get all the constructorEntities.
     *
     * @return the list of entities.
     */
    List<ConstructorEntityDTO> findAll();

    /**
     * Get the "id" constructorEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConstructorEntityDTO> findOne(String id);

    /**
     * Delete the "id" constructorEntity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    void deleteAll();

    @Deprecated
    ConstructorEntity getOrCreateConstructor(String signature, String name, String fileName, String className);
}
