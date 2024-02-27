package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;

import javax.swing.text.html.Option;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.snail.sentinel.backend.domain.JoularNodeEntity}.
 */
public interface JoularNodeEntityService {
    /**
     * Save a joularNodeEntity.
     *
     * @param joularNodeEntityDTO the entity to save.
     * @return the persisted entity.
     */
    JoularNodeEntityDTO save(JoularNodeEntityDTO joularNodeEntityDTO);

    List<JoularNodeEntityDTO> bulkAdd(List<JoularNodeEntityDTO> joularNodeEntityDTOList);

    /**
     * Updates a joularNodeEntity.
     *
     * @param joularNodeEntityDTO the entity to update.
     * @return the persisted entity.
     */
    JoularNodeEntityDTO update(JoularNodeEntityDTO joularNodeEntityDTO);

    /**
     * Partially updates a joularNodeEntity.
     *
     * @param joularNodeEntityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JoularNodeEntityDTO> partialUpdate(JoularNodeEntityDTO joularNodeEntityDTO);

    /**
     * Get all the joularNodeEntities.
     *
     * @return the list of entities.
     */
    List<JoularNodeEntityDTO> findAll();

    /**
     * Get the "id" joularNodeEntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JoularNodeEntityDTO> findOne(String id);

    /**
     * Delete the "id" joularNodeEntity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    void handleJoularNodeEntityCreationForOneIteration(Path iterationFilePath);
}
