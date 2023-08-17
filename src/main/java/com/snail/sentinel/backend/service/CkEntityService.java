package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.snail.sentinel.backend.domain.CkEntity}.
 */
public interface CkEntityService {
    /**
     * Save a ckEntity.
     *
     * @param ckEntityDTO the entity to save.
     * @return the persisted entity.
     */
    CkEntityDTO save(CkEntityDTO ckEntityDTO);

    /**
     * Updates a ckEntity.
     *
     * @param ckEntityDTO the entity to update.
     * @return the persisted entity.
     */
    CkEntityDTO update(CkEntityDTO ckEntityDTO);

    /**
     * Partially updates a ckEntity.
     *
     * @param ckEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CkEntityDTO> partialUpdate(CkEntityDTO ckEntityDTO);

    /**
     * Get all the ckEntities.
     *
     * @return the list of entities.
     */
    List<CkEntityDTO> findAll();

    /**
     * Get the "id" ckEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CkEntityDTO> findOne(String id);

    /**
     * Delete the "id" ckEntity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
