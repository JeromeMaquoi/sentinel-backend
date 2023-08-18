package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.repository.CommitEntityRepository;
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
 * REST controller for managing {@link com.snail.sentinel.backend.domain.CommitEntity}.
 */
@RestController
@RequestMapping("/api")
public class CommitEntityResource {

    private final Logger log = LoggerFactory.getLogger(CommitEntityResource.class);

    private static final String ENTITY_NAME = "commitEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommitEntityRepository commitEntityRepository;

    public CommitEntityResource(CommitEntityRepository commitEntityRepository) {
        this.commitEntityRepository = commitEntityRepository;
    }

    /**
     * {@code POST  /commit-entities} : Create a new commitEntity.
     *
     * @param commitEntity the commitEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commitEntity, or with status {@code 400 (Bad Request)} if the commitEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commit-entities")
    public ResponseEntity<CommitEntity> createCommitEntity(@Valid @RequestBody CommitEntity commitEntity) throws URISyntaxException {
        log.debug("REST request to save CommitEntity : {}", commitEntity);
        if (commitEntity.getId() != null) {
            throw new BadRequestAlertException("A new commitEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommitEntity result = commitEntityRepository.save(commitEntity);
        return ResponseEntity
            .created(new URI("/api/commit-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /commit-entities/:id} : Updates an existing commitEntity.
     *
     * @param id the id of the commitEntity to save.
     * @param commitEntity the commitEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commitEntity,
     * or with status {@code 400 (Bad Request)} if the commitEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commitEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commit-entities/{id}")
    public ResponseEntity<CommitEntity> updateCommitEntity(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody CommitEntity commitEntity
    ) throws URISyntaxException {
        log.debug("REST request to update CommitEntity : {}, {}", id, commitEntity);
        if (commitEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commitEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commitEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommitEntity result = commitEntityRepository.save(commitEntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commitEntity.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /commit-entities/:id} : Partial updates given fields of an existing commitEntity, field will ignore if it is null
     *
     * @param id the id of the commitEntity to save.
     * @param commitEntity the commitEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commitEntity,
     * or with status {@code 400 (Bad Request)} if the commitEntity is not valid,
     * or with status {@code 404 (Not Found)} if the commitEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the commitEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commit-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommitEntity> partialUpdateCommitEntity(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody CommitEntity commitEntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommitEntity partially : {}, {}", id, commitEntity);
        if (commitEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commitEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commitEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommitEntity> result = commitEntityRepository
            .findById(commitEntity.getId())
            .map(existingCommitEntity -> {
                if (commitEntity.getSha() != null) {
                    existingCommitEntity.setSha(commitEntity.getSha());
                }

                return existingCommitEntity;
            })
            .map(commitEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commitEntity.getId())
        );
    }

    /**
     * {@code GET  /commit-entities} : get all the commitEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commitEntities in body.
     */
    @GetMapping("/commit-entities")
    public List<CommitEntity> getAllCommitEntities() {
        log.debug("REST request to get all CommitEntities");
        return commitEntityRepository.findAll();
    }

    /**
     * {@code GET  /commit-entities/:id} : get the "id" commitEntity.
     *
     * @param id the id of the commitEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commitEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commit-entities/{id}")
    public ResponseEntity<CommitEntity> getCommitEntity(@PathVariable String id) {
        log.debug("REST request to get CommitEntity : {}", id);
        Optional<CommitEntity> commitEntity = commitEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commitEntity);
    }

    /**
     * {@code DELETE  /commit-entities/:id} : delete the "id" commitEntity.
     *
     * @param id the id of the commitEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commit-entities/{id}")
    public ResponseEntity<Void> deleteCommitEntity(@PathVariable String id) {
        log.debug("REST request to delete CommitEntity : {}", id);
        commitEntityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
