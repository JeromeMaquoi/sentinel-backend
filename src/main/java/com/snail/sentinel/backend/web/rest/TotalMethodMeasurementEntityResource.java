package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.TotalMethodMeasurementRepository;
import com.snail.sentinel.backend.service.MeasurementService;
import com.snail.sentinel.backend.service.dto.TotalMethodMeasurementEntityDTO;
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
@RequestMapping("/api/v2/measurements/total/methods")
public class TotalMethodMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(TotalMethodMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "totalMethodMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final MeasurementService<TotalMethodMeasurementEntityDTO> service;
    private final TotalMethodMeasurementRepository repository;

    public TotalMethodMeasurementEntityResource(MeasurementService<TotalMethodMeasurementEntityDTO> service, TotalMethodMeasurementRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("")
    public ResponseEntity<TotalMethodMeasurementEntityDTO> createTotalMethodMeasurementEntity(@RequestBody TotalMethodMeasurementEntityDTO totalMethodMeasurementEntityDTO) throws URISyntaxException {
        log.debug("Request to create Total Method Measurement Entity : {}", totalMethodMeasurementEntityDTO);
        if (totalMethodMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new totalMethodMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TotalMethodMeasurementEntityDTO result = service.save(totalMethodMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/measurements/total/methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TotalMethodMeasurementEntityDTO>> bulkAddTotalMethodMeasurementEntities(@RequestBody List<TotalMethodMeasurementEntityDTO> totalMethodMeasurementEntityDTOList) {
        log.debug("Request to bulk add Total Method Measurement Entities : {}", totalMethodMeasurementEntityDTOList);
        List<TotalMethodMeasurementEntityDTO> result = service.bulkAdd(totalMethodMeasurementEntityDTOList);
        return new ResponseEntity<>(result, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TotalMethodMeasurementEntityDTO> updateTotalMethodMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TotalMethodMeasurementEntityDTO totalMethodMeasurementEntityDTO
    ) {
        log.debug("Request to update Total Method Measurement Entity : {}, {}", id, totalMethodMeasurementEntityDTO);
        if (totalMethodMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(totalMethodMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        TotalMethodMeasurementEntityDTO result = service.update(totalMethodMeasurementEntityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TotalMethodMeasurementEntityDTO> partialUpdateTotalMethodMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody TotalMethodMeasurementEntityDTO totalMethodMeasurementEntityDTO
    ) {
        log.debug("Request to partially update Total Method Measurement Entity : {}, {}", id, totalMethodMeasurementEntityDTO);
        if (totalMethodMeasurementEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!id.equals(totalMethodMeasurementEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<TotalMethodMeasurementEntityDTO> result = service.partialUpdate(totalMethodMeasurementEntityDTO);
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, totalMethodMeasurementEntityDTO.getId()));
    }

    @GetMapping("")
    public ResponseEntity<List<TotalMethodMeasurementEntityDTO>> getAllTotalMethodMeasurementEntities() {
        log.debug("Request to get all Total Method Measurement Entities");
        List<TotalMethodMeasurementEntityDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TotalMethodMeasurementEntityDTO> getTotalMethodMeasurementEntity(@PathVariable String id) {
        log.debug("Request to get Total Method Measurement Entity : {}", id);
        Optional<TotalMethodMeasurementEntityDTO> totalMethodMeasurementEntityDTO = service.findOne(id);
        return ResponseUtil.wrapOrNotFound(totalMethodMeasurementEntityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTotalMethodMeasurementEntity(@PathVariable String id) {
        log.debug("Request to delete Total Method Measurement Entity : {}", id);
        service.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
            .build();
    }
}
