package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.StackTraceElementJoularEntityRepository;
import com.snail.sentinel.backend.service.StackTraceElementJoularEntityService;
import com.snail.sentinel.backend.service.dto.StackTraceElementJoularEntityDTO;
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
 * REST controller for managing {@link com.snail.sentinel.backend.domain.StackTraceElementJoularEntity}.
 */
@RestController
@RequestMapping("/api/stack-trace-element-joular-entities")
public class StackTraceElementJoularEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(StackTraceElementJoularEntityResource.class);

    private static final String ENTITY_NAME = "stackTraceElementJoularEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StackTraceElementJoularEntityService stackTraceElementJoularEntityService;

    private final StackTraceElementJoularEntityRepository stackTraceElementJoularEntityRepository;

    public StackTraceElementJoularEntityResource(
        StackTraceElementJoularEntityService stackTraceElementJoularEntityService,
        StackTraceElementJoularEntityRepository stackTraceElementJoularEntityRepository
    ) {
        this.stackTraceElementJoularEntityService = stackTraceElementJoularEntityService;
        this.stackTraceElementJoularEntityRepository = stackTraceElementJoularEntityRepository;
    }

    /**
     * {@code POST  /stack-trace-element-joular-entities} : Create a new stackTraceElementJoularEntity.
     *
     * @param stackTraceElementJoularEntityDTO the stackTraceElementJoularEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stackTraceElementJoularEntityDTO, or with status {@code 400 (Bad Request)} if the stackTraceElementJoularEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StackTraceElementJoularEntityDTO> createStackTraceElementJoularEntity(
        @RequestBody StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save StackTraceElementJoularEntity : {}", stackTraceElementJoularEntityDTO);
        if (stackTraceElementJoularEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new stackTraceElementJoularEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityService.save(stackTraceElementJoularEntityDTO);
        return ResponseEntity.created(new URI("/api/stack-trace-element-joular-entities/" + stackTraceElementJoularEntityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, stackTraceElementJoularEntityDTO.getId()))
            .body(stackTraceElementJoularEntityDTO);
    }

    /**
     * {@code PUT  /stack-trace-element-joular-entities/:id} : Updates an existing stackTraceElementJoularEntity.
     *
     * @param id the id of the stackTraceElementJoularEntityDTO to save.
     * @param stackTraceElementJoularEntityDTO the stackTraceElementJoularEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stackTraceElementJoularEntityDTO,
     * or with status {@code 400 (Bad Request)} if the stackTraceElementJoularEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stackTraceElementJoularEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StackTraceElementJoularEntityDTO> updateStackTraceElementJoularEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StackTraceElementJoularEntity : {}, {}", id, stackTraceElementJoularEntityDTO);
        if (stackTraceElementJoularEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stackTraceElementJoularEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stackTraceElementJoularEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityService.update(stackTraceElementJoularEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stackTraceElementJoularEntityDTO.getId()))
            .body(stackTraceElementJoularEntityDTO);
    }

    /**
     * {@code PATCH  /stack-trace-element-joular-entities/:id} : Partial updates given fields of an existing stackTraceElementJoularEntity, field will ignore if it is null
     *
     * @param id the id of the stackTraceElementJoularEntityDTO to save.
     * @param stackTraceElementJoularEntityDTO the stackTraceElementJoularEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stackTraceElementJoularEntityDTO,
     * or with status {@code 400 (Bad Request)} if the stackTraceElementJoularEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stackTraceElementJoularEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stackTraceElementJoularEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StackTraceElementJoularEntityDTO> partialUpdateStackTraceElementJoularEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody StackTraceElementJoularEntityDTO stackTraceElementJoularEntityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StackTraceElementJoularEntity partially : {}, {}", id, stackTraceElementJoularEntityDTO);
        if (stackTraceElementJoularEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stackTraceElementJoularEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stackTraceElementJoularEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StackTraceElementJoularEntityDTO> result = stackTraceElementJoularEntityService.partialUpdate(
            stackTraceElementJoularEntityDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stackTraceElementJoularEntityDTO.getId())
        );
    }

    /**
     * {@code GET  /stack-trace-element-joular-entities} : get all the stackTraceElementJoularEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stackTraceElementJoularEntities in body.
     */
    @GetMapping("")
    public List<StackTraceElementJoularEntityDTO> getAllStackTraceElementJoularEntities() {
        LOG.debug("REST request to get all StackTraceElementJoularEntities");
        return stackTraceElementJoularEntityService.findAll();
    }

    /**
     * {@code GET  /stack-trace-element-joular-entities/:id} : get the "id" stackTraceElementJoularEntity.
     *
     * @param id the id of the stackTraceElementJoularEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stackTraceElementJoularEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StackTraceElementJoularEntityDTO> getStackTraceElementJoularEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to get StackTraceElementJoularEntity : {}", id);
        Optional<StackTraceElementJoularEntityDTO> stackTraceElementJoularEntityDTO = stackTraceElementJoularEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stackTraceElementJoularEntityDTO);
    }

    /**
     * {@code DELETE  /stack-trace-element-joular-entities/:id} : delete the "id" stackTraceElementJoularEntity.
     *
     * @param id the id of the stackTraceElementJoularEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStackTraceElementJoularEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete StackTraceElementJoularEntity : {}", id);
        stackTraceElementJoularEntityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
