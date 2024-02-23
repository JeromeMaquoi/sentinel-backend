package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.JoularNodeEntityService;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.snail.sentinel.backend.domain.JoularNodeEntity}.
 */
@RestController
@RequestMapping("/api/v1/joular-node-entities")
public class JoularNodeEntityResource {

    private final Logger log = LoggerFactory.getLogger(JoularNodeEntityResource.class);

    private static final String ENTITY_NAME = "joularNodeEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JoularNodeEntityService joularNodeEntityService;

    private final JoularNodeEntityRepository joularNodeEntityRepository;

    public JoularNodeEntityResource(
        JoularNodeEntityService joularNodeEntityService,
        JoularNodeEntityRepository joularNodeEntityRepository
    ) {
        this.joularNodeEntityService = joularNodeEntityService;
        this.joularNodeEntityRepository = joularNodeEntityRepository;
    }

    /**
     * {@code POST  /joular-node-entities} : Create a new joularNodeEntity.
     *
     * @param joularNodeEntityDTO the joularNodeEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new joularNodeEntityDTO, or with status {@code 400 (Bad Request)} if the joularNodeEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JoularNodeEntityDTO> createJoularNodeEntity(@RequestBody JoularNodeEntityDTO joularNodeEntityDTO)
        throws URISyntaxException {
        log.debug("REST request to save JoularNodeEntity : {}", joularNodeEntityDTO);
        if (joularNodeEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new joularNodeEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JoularNodeEntityDTO result = joularNodeEntityService.save(joularNodeEntityDTO);
        return ResponseEntity
            .created(new URI("/api/joular-node-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /joular-node-entities/:id} : Updates an existing joularNodeEntity.
     *
     * @param id the id of the joularNodeEntityDTO to save.
     * @param joularNodeEntityDTO the joularNodeEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joularNodeEntityDTO,
     * or with status {@code 400 (Bad Request)} if the joularNodeEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the joularNodeEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JoularNodeEntityDTO> updateJoularNodeEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody JoularNodeEntityDTO joularNodeEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JoularNodeEntity : {}, {}", id, joularNodeEntityDTO);
        if (joularNodeEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, joularNodeEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!joularNodeEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JoularNodeEntityDTO result = joularNodeEntityService.update(joularNodeEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, joularNodeEntityDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /joular-node-entities/:id} : Partial updates given fields of an existing joularNodeEntity, field will ignore if it is null
     *
     * @param id the id of the joularNodeEntityDTO to save.
     * @param joularNodeEntityDTO the joularNodeEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joularNodeEntityDTO,
     * or with status {@code 400 (Bad Request)} if the joularNodeEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the joularNodeEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the joularNodeEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JoularNodeEntityDTO> partialUpdateJoularNodeEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody JoularNodeEntityDTO joularNodeEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JoularNodeEntity partially : {}, {}", id, joularNodeEntityDTO);
        if (joularNodeEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, joularNodeEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!joularNodeEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JoularNodeEntityDTO> result = joularNodeEntityService.partialUpdate(joularNodeEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, joularNodeEntityDTO.getId())
        );
    }

    /**
     * {@code GET  /joular-node-entities} : get all the joularNodeEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of joularNodeEntities in body.
     */
    @GetMapping("")
    public List<JoularNodeEntityDTO> getAllJoularNodeEntities() {
        log.debug("REST request to get all JoularNodeEntities");
        return joularNodeEntityService.findAll();
    }

    /**
     * {@code GET  /joular-node-entities/:id} : get the "id" joularNodeEntity.
     *
     * @param id the id of the joularNodeEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the joularNodeEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JoularNodeEntityDTO> getJoularNodeEntity(@PathVariable("id") String id) {
        log.debug("REST request to get JoularNodeEntity : {}", id);
        Optional<JoularNodeEntityDTO> joularNodeEntityDTO = joularNodeEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(joularNodeEntityDTO);
    }

    /**
     * {@code DELETE  /joular-node-entities/:id} : delete the "id" joularNodeEntity.
     *
     * @param id the id of the joularNodeEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoularNodeEntity(@PathVariable("id") String id) {
        log.debug("REST request to delete JoularNodeEntity : {}", id);
        joularNodeEntityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
