package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.domain.AttributeEntity;
import com.snail.sentinel.backend.repository.AttributeEntityRepository;
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
 * REST controller for managing {@link com.snail.sentinel.backend.domain.AttributeEntity}.
 */
@RestController
@RequestMapping("/api/attribute-entities")
public class AttributeEntityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AttributeEntityResource.class);

    private static final String ENTITY_NAME = "attributeEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributeEntityRepository attributeEntityRepository;

    public AttributeEntityResource(AttributeEntityRepository attributeEntityRepository) {
        this.attributeEntityRepository = attributeEntityRepository;
    }

    /**
     * {@code POST  /attribute-entities} : Create a new attributeEntity.
     *
     * @param attributeEntity the attributeEntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributeEntity, or with status {@code 400 (Bad Request)} if the attributeEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttributeEntity> createAttributeEntity(@RequestBody AttributeEntity attributeEntity) throws URISyntaxException {
        LOG.debug("REST request to save AttributeEntity : {}", attributeEntity);
        if (attributeEntity.getId() != null) {
            throw new BadRequestAlertException("A new attributeEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        attributeEntity = attributeEntityRepository.save(attributeEntity);
        return ResponseEntity.created(new URI("/api/attribute-entities/" + attributeEntity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, attributeEntity.getId()))
            .body(attributeEntity);
    }

    /**
     * {@code PUT  /attribute-entities/:id} : Updates an existing attributeEntity.
     *
     * @param id the id of the attributeEntity to save.
     * @param attributeEntity the attributeEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeEntity,
     * or with status {@code 400 (Bad Request)} if the attributeEntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributeEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttributeEntity> updateAttributeEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttributeEntity attributeEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to update AttributeEntity : {}, {}", id, attributeEntity);
        if (attributeEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        attributeEntity = attributeEntityRepository.save(attributeEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributeEntity.getId()))
            .body(attributeEntity);
    }

    /**
     * {@code PATCH  /attribute-entities/:id} : Partial updates given fields of an existing attributeEntity, field will ignore if it is null
     *
     * @param id the id of the attributeEntity to save.
     * @param attributeEntity the attributeEntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributeEntity,
     * or with status {@code 400 (Bad Request)} if the attributeEntity is not valid,
     * or with status {@code 404 (Not Found)} if the attributeEntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributeEntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttributeEntity> partialUpdateAttributeEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttributeEntity attributeEntity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AttributeEntity partially : {}, {}", id, attributeEntity);
        if (attributeEntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributeEntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributeEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttributeEntity> result = attributeEntityRepository
            .findById(attributeEntity.getId())
            .map(existingAttributeEntity -> {
                if (attributeEntity.getName() != null) {
                    existingAttributeEntity.setName(attributeEntity.getName());
                }
                if (attributeEntity.getType() != null) {
                    existingAttributeEntity.setType(attributeEntity.getType());
                }

                return existingAttributeEntity;
            })
            .map(attributeEntityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attributeEntity.getId())
        );
    }

    /**
     * {@code GET  /attribute-entities} : get all the attributeEntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributeEntities in body.
     */
    @GetMapping("")
    public List<AttributeEntity> getAllAttributeEntities() {
        LOG.debug("REST request to get all AttributeEntities");
        return attributeEntityRepository.findAll();
    }

    /**
     * {@code GET  /attribute-entities/:id} : get the "id" attributeEntity.
     *
     * @param id the id of the attributeEntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributeEntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttributeEntity> getAttributeEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to get AttributeEntity : {}", id);
        Optional<AttributeEntity> attributeEntity = attributeEntityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attributeEntity);
    }

    /**
     * {@code DELETE  /attribute-entities/:id} : delete the "id" attributeEntity.
     *
     * @param id the id of the attributeEntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeEntity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete AttributeEntity : {}", id);
        attributeEntityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
