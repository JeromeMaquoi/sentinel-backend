package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.CallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.CallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.CallTreeMeasurementEntityDTO;
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
@RequestMapping("/api/v2/call-tree-measurements-entities")
public class CallTreeMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(CallTreeMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "callTreeMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final CallTreeMeasurementService service;
    private final CallTreeMeasurementRepository repository;

    public CallTreeMeasurementEntityResource(CallTreeMeasurementService service, CallTreeMeasurementRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("")
    public ResponseEntity<CallTreeMeasurementEntityDTO> createCallTreeMeasurementEntity(@RequestBody CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO) throws URISyntaxException {
        log.debug("REST request to save CallTreeMeasurementEntity : {}", callTreeMeasurementEntityDTO);
        if (callTreeMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new callTreeMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CallTreeMeasurementEntityDTO result = service.save(callTreeMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/call-tree-measurements-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CallTreeMeasurementEntityDTO>> bulkAddCallTreeMeasurementEntities(@RequestBody List<CallTreeMeasurementEntityDTO> callTreeMeasurementEntityDTOList) {
        log.debug("REST request to bulkAddCallTreeMeasurementEntities : {}", callTreeMeasurementEntityDTOList);
        List<CallTreeMeasurementEntityDTO> result = service.bulkAdd(callTreeMeasurementEntityDTOList);
        return new ResponseEntity<>(result, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CallTreeMeasurementEntityDTO> updateCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CallTreeMeasurementEntity : {}, {}", id, callTreeMeasurementEntityDTO);
        if (callTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(callTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        CallTreeMeasurementEntityDTO result = service.update(callTreeMeasurementEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CallTreeMeasurementEntityDTO> partialUpdateCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CallTreeMeasurementEntityDTO callTreeMeasurementEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CallTreeMeasurementEntity : {}, {}", id, callTreeMeasurementEntityDTO);
        if (callTreeMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(callTreeMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<CallTreeMeasurementEntityDTO> result = service.partialUpdate(callTreeMeasurementEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, callTreeMeasurementEntityDTO.getId())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<CallTreeMeasurementEntityDTO>> getAllCallTreeMeasurementEntitys() {
        log.debug("REST request to get all CallTreeMeasurementEntities");
        List<CallTreeMeasurementEntityDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CallTreeMeasurementEntityDTO> getCallTreeMeasurementEntity(@PathVariable("id") String id) {
        log.debug("REST request to get CallTreeMeasurementEntity : {}", id);
        Optional<CallTreeMeasurementEntityDTO> callTreeMeasurementEntityDTO = service.findOne(id);
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
