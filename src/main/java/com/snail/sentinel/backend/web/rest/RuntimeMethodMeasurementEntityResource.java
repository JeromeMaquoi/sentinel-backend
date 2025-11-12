package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.RuntimeMethodMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeMethodMeasurementEntityDTO;
import com.snail.sentinel.backend.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/measurements/runtime/methods")
public class RuntimeMethodMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(RuntimeMethodMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "methodMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final MeasurementService<RuntimeMethodMeasurementEntityDTO> service;
    private final RuntimeMethodMeasurementRepository repository;

    public RuntimeMethodMeasurementEntityResource(MeasurementService<RuntimeMethodMeasurementEntityDTO> service, RuntimeMethodMeasurementRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("")
    public ResponseEntity<RuntimeMethodMeasurementEntityDTO> createMethodMeasurementEntity(@RequestBody RuntimeMethodMeasurementEntityDTO runtimeMethodMeasurementEntityDTO) throws URISyntaxException {
        log.debug("Request to create RuntimeMethodMeasurementEntity : {}", runtimeMethodMeasurementEntityDTO);
        if (runtimeMethodMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new methodMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuntimeMethodMeasurementEntityDTO result = service.save(runtimeMethodMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/measurements/runtime/methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RuntimeMethodMeasurementEntityDTO>> bulkAddMethodMeasurementEntities(@RequestBody List<RuntimeMethodMeasurementEntityDTO> runtimeMethodMeasurementEntityDTOList) {
        log.debug("Request to bulk add RuntimeMethodMeasurementEntities : {}", runtimeMethodMeasurementEntityDTOList);
        List<RuntimeMethodMeasurementEntityDTO> result = service.bulkAdd(runtimeMethodMeasurementEntityDTOList);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuntimeMethodMeasurementEntityDTO> updateMethodMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RuntimeMethodMeasurementEntityDTO runtimeMethodMeasurementEntityDTO
    ) {
        log.debug("Request to update RuntimeMethodMeasurementEntity : {}, {}", id, runtimeMethodMeasurementEntityDTO);
        if (runtimeMethodMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(runtimeMethodMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        RuntimeMethodMeasurementEntityDTO result = service.update(runtimeMethodMeasurementEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RuntimeMethodMeasurementEntityDTO> partialUpdateMethodMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RuntimeMethodMeasurementEntityDTO runtimeMethodMeasurementEntityDTO
    ) {
        log.debug("Request to partially update RuntimeMethodMeasurementEntity : {}, {}", id, runtimeMethodMeasurementEntityDTO);
        if (runtimeMethodMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(runtimeMethodMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<RuntimeMethodMeasurementEntityDTO> result = service.partialUpdate(runtimeMethodMeasurementEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, runtimeMethodMeasurementEntityDTO.getId())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<RuntimeMethodMeasurementEntityDTO>> getAllMethodMeasurementEntities() {
        log.debug("Request to get all RuntimeMethodMeasurementEntities");
        List<RuntimeMethodMeasurementEntityDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RuntimeMethodMeasurementEntityDTO> getMethodMeasurementEntity(@PathVariable String id) {
        log.debug("Request to get RuntimeMethodMeasurementEntity : {}", id);
        Optional<RuntimeMethodMeasurementEntityDTO> runtimeMethodMeasurementEntityDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(runtimeMethodMeasurementEntityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMethodMeasurementEntity(@PathVariable String id) {
        log.debug("Request to delete RuntimeMethodMeasurementEntity : {}", id);
        service.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
            .build();
    }
}
