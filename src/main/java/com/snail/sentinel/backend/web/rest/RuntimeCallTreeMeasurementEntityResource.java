package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
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
@RequestMapping("/api/v2/measurements/runtime/calltrees")
public class RuntimeCallTreeMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "callTreeMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final RuntimeCallTreeMeasurementService service;
    private final RuntimeCallTreeMeasurementRepository repository;

    public RuntimeCallTreeMeasurementEntityResource(RuntimeCallTreeMeasurementService service, RuntimeCallTreeMeasurementRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> createCallTreeMeasurementEntity(@RequestBody RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) throws URISyntaxException {
        log.debug("REST request to save CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        if (runtimeCallTreeMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new callTreeMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuntimeCallTreeMeasurementEntityDTO result = service.save(runtimeCallTreeMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/call-tree-measurements-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RuntimeCallTreeMeasurementEntityDTO>> bulkAddCallTreeMeasurementEntities(@RequestBody List<RuntimeCallTreeMeasurementEntityDTO> runtimeCallTreeMeasurementEntityDTOList) {
        log.debug("REST request to bulkAddCallTreeMeasurementEntities : {}", runtimeCallTreeMeasurementEntityDTOList);
        List<RuntimeCallTreeMeasurementEntityDTO> result = service.bulkAdd(runtimeCallTreeMeasurementEntityDTOList);
        return new ResponseEntity<>(result, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> updateCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CallTreeMeasurementEntity : {}, {}", id, runtimeCallTreeMeasurementEntityDTO);
        if (runtimeCallTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(runtimeCallTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        RuntimeCallTreeMeasurementEntityDTO result = service.update(runtimeCallTreeMeasurementEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> partialUpdateCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CallTreeMeasurementEntity : {}, {}", id, runtimeCallTreeMeasurementEntityDTO);
        if (runtimeCallTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(runtimeCallTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<RuntimeCallTreeMeasurementEntityDTO> result = service.partialUpdate(runtimeCallTreeMeasurementEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, runtimeCallTreeMeasurementEntityDTO.getId())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<RuntimeCallTreeMeasurementEntityDTO>> getAllCallTreeMeasurementEntitys() {
        log.debug("REST request to get all CallTreeMeasurementEntities");
        List<RuntimeCallTreeMeasurementEntityDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> getCallTreeMeasurementEntity(@PathVariable("id") String id) {
        log.debug("REST request to get CallTreeMeasurementEntity : {}", id);
        Optional<RuntimeCallTreeMeasurementEntityDTO> callTreeMeasurementEntityDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(callTreeMeasurementEntityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCallTreeMeasurementEntity(@PathVariable("id") String id) {
        log.debug("REST request to delete CallTreeMeasurementEntity : {}", id);
        service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
            .build();
    }
}
