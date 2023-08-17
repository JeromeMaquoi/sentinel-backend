package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.service.CkEntityService;
import com.snail.sentinel.backend.service.dto.CkEntityDTO;
import com.snail.sentinel.backend.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.snail.sentinel.backend.domain.CkEntity}.
 */
@RestController
@RequestMapping("/api")
public class CkEntityResource {

    private final Logger log = LoggerFactory.getLogger(CkEntityResource.class);

    private static final String ENTITY_NAME = "ckEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CkEntityService ckEntityService;

    private final CkEntityRepository ckEntityRepository;

    public CkEntityResource(CkEntityService ckEntityService, CkEntityRepository ckEntityRepository) {
        this.ckEntityService = ckEntityService;
        this.ckEntityRepository = ckEntityRepository;
    }

    /**
     * {@code POST  /ck-entities} : Create a new ckEntity.
     *
     * @param ckEntityDTO the ckEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ckEntityDTO, or with status {@code 400 (Bad Request)} if the ckEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ck-entities")
    public ResponseEntity<CkEntityDTO> createCkEntity(@Valid @RequestBody CkEntityDTO ckEntityDTO) throws URISyntaxException {
        log.debug("REST request to save CkEntity : {}", ckEntityDTO);
        if (ckEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new ckEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CkEntityDTO result = ckEntityService.save(ckEntityDTO);
        return ResponseEntity
            .created(new URI("/api/ck-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /ck-entities/:id} : Updates an existing ckEntity.
     *
     * @param id the id of the ckEntityDTO to save.
     * @param ckEntityDTO the ckEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ckEntityDTO,
     * or with status {@code 400 (Bad Request)} if the ckEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ckEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ck-entities/{id}")
    public ResponseEntity<CkEntityDTO> updateCkEntity(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CkEntityDTO ckEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CkEntity : {}, {}", id, ckEntityDTO);
        if (ckEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ckEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ckEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CkEntityDTO result = ckEntityService.update(ckEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ckEntityDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /ck-entities/:id} : Partial updates given fields of an existing ckEntity, field will ignore if it is null
     *
     * @param id the id of the ckEntityDTO to save.
     * @param ckEntityDTO the ckEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ckEntityDTO,
     * or with status {@code 400 (Bad Request)} if the ckEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ckEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ckEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ck-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CkEntityDTO> partialUpdateCkEntity(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CkEntityDTO ckEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CkEntity partially : {}, {}", id, ckEntityDTO);
        if (ckEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ckEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ckEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CkEntityDTO> result = ckEntityService.partialUpdate(ckEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ckEntityDTO.getId())
        );
    }

    /**
     * {@code GET  /ck-entities} : get all the ckEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ckEntities in body.
     */
    @GetMapping("/ck-entities")
    public List<CkEntityDTO> getAllCkEntities() {
        log.debug("REST request to get all CkEntities");
        return ckEntityService.findAll();
    }

    /**
     * {@code GET  /ck-entities/:id} : get the "id" ckEntity.
     *
     * @param id the id of the ckEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ckEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ck-entities/{id}")
    public ResponseEntity<CkEntityDTO> getCkEntity(@PathVariable String id) {
        log.debug("REST request to get CkEntity : {}", id);
        Optional<CkEntityDTO> ckEntityDTO = ckEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ckEntityDTO);
    }

    /**
     * {@code DELETE  /ck-entities/:id} : delete the "id" ckEntity.
     *
     * @param id the id of the ckEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ck-entities/{id}")
    public ResponseEntity<Void> deleteCkEntity(@PathVariable String id) {
        log.debug("REST request to delete CkEntity : {}", id);
        ckEntityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
