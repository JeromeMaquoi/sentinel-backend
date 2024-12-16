package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.ConstructorEntityRepository;
import com.snail.sentinel.backend.service.ConstructorAttributeService;
import com.snail.sentinel.backend.service.ConstructorEntityService;
import com.snail.sentinel.backend.service.dto.ConstructorEntityDTO;
import com.snail.sentinel.backend.service.dto.RegisterAttributeRequest;
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
 * REST controller for managing {@link com.snail.sentinel.backend.domain.ConstructorEntity}.
 */
@RestController
@RequestMapping("/api/v1/constructor-entities")
public class ConstructorEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConstructorEntityResource.class);

    private static final String ENTITY_NAME = "constructorEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConstructorEntityService constructorEntityService;

    private final ConstructorEntityRepository constructorEntityRepository;

    private final ConstructorAttributeService constructorAttributeService;

    public ConstructorEntityResource(
        ConstructorEntityService constructorEntityService,
        ConstructorEntityRepository constructorEntityRepository,
        ConstructorAttributeService constructorAttributeService
    ) {
        this.constructorEntityService = constructorEntityService;
        this.constructorEntityRepository = constructorEntityRepository;
        this.constructorAttributeService = constructorAttributeService;
    }

    /**
     * {@code POST  /constructor-entities} : Create a new constructorEntity.
     *
     * @param constructorEntityDTO the constructorEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new constructorEntityDTO, or with status {@code 400 (Bad Request)} if the constructorEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("")
    public ResponseEntity<ConstructorEntityDTO> createConstructorEntity(@RequestBody ConstructorEntityDTO constructorEntityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ConstructorEntity : {}", constructorEntityDTO);
        if (constructorEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new constructorEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        constructorEntityDTO = constructorEntityService.save(constructorEntityDTO);
        return ResponseEntity.created(new URI("/api/v1/constructor-entities/" + constructorEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, constructorEntityDTO.getId()))
            .body(constructorEntityDTO);
    }*/

    @PostMapping("")
    public ResponseEntity<ConstructorEntityDTO> registerConstructorEntityAttributes(@RequestBody RegisterAttributeRequest registerAttributeRequest) throws URISyntaxException {
        ConstructorEntityDTO constructorEntityDTO = constructorAttributeService.registerAttribute(registerAttributeRequest);
        return ResponseEntity.created(new URI("/api/v1/constructor-entities/" + constructorEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, constructorEntityDTO.getId()))
            .body(constructorEntityDTO);
    }

    /**
     * {@code PUT  /constructor-entities/:id} : Updates an existing constructorEntity.
     *
     * @param id the id of the constructorEntityDTO to save.
     * @param constructorEntityDTO the constructorEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constructorEntityDTO,
     * or with status {@code 400 (Bad Request)} if the constructorEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the constructorEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConstructorEntityDTO> updateConstructorEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ConstructorEntityDTO constructorEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConstructorEntity : {}, {}", id, constructorEntityDTO);
        if (constructorEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constructorEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constructorEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        constructorEntityDTO = constructorEntityService.update(constructorEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, constructorEntityDTO.getId()))
            .body(constructorEntityDTO);
    }

    /**
     * {@code PATCH  /constructor-entities/:id} : Partial updates given fields of an existing constructorEntity, field will ignore if it is null
     *
     * @param id the id of the constructorEntityDTO to save.
     * @param constructorEntityDTO the constructorEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated constructorEntityDTO,
     * or with status {@code 400 (Bad Request)} if the constructorEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the constructorEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the constructorEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConstructorEntityDTO> partialUpdateConstructorEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ConstructorEntityDTO constructorEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ConstructorEntity partially : {}, {}", id, constructorEntityDTO);
        if (constructorEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, constructorEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!constructorEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConstructorEntityDTO> result = constructorEntityService.partialUpdate(constructorEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, constructorEntityDTO.getId())
        );
    }

    /**
     * {@code GET  /constructor-entities} : get all the constructorEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of constructorEntities in body.
     */
    @GetMapping("")
    public List<ConstructorEntityDTO> getAllConstructorEntities() {
        LOG.debug("REST request to get all ConstructorEntities");
        return constructorEntityService.findAll();
    }

    /**
     * {@code GET  /constructor-entities/:id} : get the "id" constructorEntity.
     *
     * @param id the id of the constructorEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the constructorEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConstructorEntityDTO> getConstructorEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to get ConstructorEntity : {}", id);
        Optional<ConstructorEntityDTO> constructorEntityDTO = constructorEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(constructorEntityDTO);
    }

    /**
     * {@code DELETE  /constructor-entities/:id} : delete the "id" constructorEntity.
     *
     * @param id the id of the constructorEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstructorEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ConstructorEntity : {}", id);
        constructorEntityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAllConstructorEntities() {
        LOG.debug("REST request to delete all ConstructorEntities");
        constructorEntityService.deleteAll();
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, "all")).build();
    }
}
