package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.TotalCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/measurements/total/calltrees")
public class TotalCallTreeMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(TotalCallTreeMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "totalCallTreeMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final MeasurementService<TotalCallTreeMeasurementEntityDTO> service;
    private final TotalCallTreeMeasurementRepository repository;

    public TotalCallTreeMeasurementEntityResource(MeasurementService<TotalCallTreeMeasurementEntityDTO> service, TotalCallTreeMeasurementRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("")
    public ResponseEntity<TotalCallTreeMeasurementEntityDTO> createTotalCallTreeMeasurementEntity(@RequestBody TotalCallTreeMeasurementEntityDTO totalCallTreeMeasurementEntityDTO) throws URISyntaxException {
        log.debug("REST request to save TotalCallTreeMeasurementEntity : {}", totalCallTreeMeasurementEntityDTO);
        if (totalCallTreeMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new totalCallTreeMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TotalCallTreeMeasurementEntityDTO result = service.save(totalCallTreeMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/measurements/total/calltrees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TotalCallTreeMeasurementEntityDTO>> bulkAddTotalCallTreeMeasurementEntities(@RequestBody List<TotalCallTreeMeasurementEntityDTO> totalCallTreeMeasurementEntityDTOList) {
        log.debug("REST request to bulkAddTotalCallTreeMeasurementEntities : {}", totalCallTreeMeasurementEntityDTOList);
        List<TotalCallTreeMeasurementEntityDTO> result = service.bulkAdd(totalCallTreeMeasurementEntityDTOList);
        return new ResponseEntity<>(result, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TotalCallTreeMeasurementEntityDTO> updateTotalCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TotalCallTreeMeasurementEntityDTO totalCallTreeMeasurementEntityDTO
    ) {
        log.debug("REST request to update TotalCallTreeMeasurementEntity : {}, {}", id, totalCallTreeMeasurementEntityDTO);
        if (totalCallTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(totalCallTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        TotalCallTreeMeasurementEntityDTO result = service.update(totalCallTreeMeasurementEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TotalCallTreeMeasurementEntityDTO> partialUpdateTotalCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TotalCallTreeMeasurementEntityDTO totalCallTreeMeasurementEntityDTO
    ) {
        log.debug("REST request to partially update TotalCallTreeMeasurementEntity : {}, {}", id, totalCallTreeMeasurementEntityDTO);
        if (totalCallTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(totalCallTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<TotalCallTreeMeasurementEntityDTO> result = service.partialUpdate(totalCallTreeMeasurementEntityDTO);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, totalCallTreeMeasurementEntityDTO.getId()));
    }

    @GetMapping("")
    public ResponseEntity<List<TotalCallTreeMeasurementEntityDTO>> getAllTotalCallTreeMeasurementEntities() {
        log.debug("REST request to get all TotalCallTreeMeasurementEntities");
        List<TotalCallTreeMeasurementEntityDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TotalCallTreeMeasurementEntityDTO> getTotalCallTreeMeasurementEntity(@PathVariable String id) {
        log.debug("REST request to get TotalCallTreeMeasurementEntity : {}", id);
        Optional<TotalCallTreeMeasurementEntityDTO> totalCallTreeMeasurementEntityDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(totalCallTreeMeasurementEntityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTotalCallTreeMeasurementEntity(@PathVariable String id) {
        log.debug("REST request to delete TotalCallTreeMeasurementEntity : {}", id);
        service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
            .build();
    }
}
