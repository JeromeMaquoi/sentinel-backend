package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.StackTraceElementJoularEntityDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.snail.sentinel.backend.domain.StackTraceElementJoularEntity}.
 */
public interface StackTraceElementJoularEntityService {
    /**
     * Save a stackTraceElementJoularEntity.
     *
     * @param stackTraceElementJoularEntityDTO the entity to save.
     * @return the persisted entity.
     */
    StackTraceElementJoularEntityDTO save(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO);

    /**
     * Updates a stackTraceElementJoularEntity.
     *
     * @param stackTraceElementJoularEntityDTO the entity to update.
     * @return the persisted entity.
     */
    StackTraceElementJoularEntityDTO update(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO);

    /**
     * Partially updates a stackTraceElementJoularEntity.
     *
     * @param stackTraceElementJoularEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StackTraceElementJoularEntityDTO> partialUpdate(StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO);

    /**
     * Get all the stackTraceElementJoularEntities.
     *
     * @return the list of entities.
     */
    List<StackTraceElementJoularEntityDTO> findAll();

    /**
     * Get the "id" stackTraceElementJoularEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StackTraceElementJoularEntityDTO> findOne(String id);

    /**
     * Delete the "id" stackTraceElementJoularEntity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
