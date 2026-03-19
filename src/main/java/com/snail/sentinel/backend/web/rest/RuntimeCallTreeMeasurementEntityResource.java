package com.snail.sentinel.backend.web.rest;

import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.service.RuntimeCallTreeAggregationPostProcessor;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
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
@RequestMapping("/api/v2/measurements/runtime/calltrees")
public class RuntimeCallTreeMeasurementEntityResource {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeMeasurementEntityResource.class);
    private static final String ENTITY_NAME = "callTreeMeasurementEntity";
    @Value("sentinelBackendApp")
    private String applicationName;
    private final RuntimeCallTreeMeasurementService service;
    private final RuntimeCallTreeMeasurementRepository repository;
    private final RuntimeCallTreeAggregationPostProcessor postProcessor;

    public RuntimeCallTreeMeasurementEntityResource(
            RuntimeCallTreeMeasurementService service,
            RuntimeCallTreeMeasurementRepository repository,
            RuntimeCallTreeAggregationPostProcessor postProcessor) {
        this.service = service;
        this.repository = repository;
        this.postProcessor = postProcessor;
    }

    @PostMapping("")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> createCallTreeMeasurementEntity(@RequestBody RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) throws URISyntaxException {
        log.debug("REST request to save CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        if (runtimeCallTreeMeasurementEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new callTreeMeasurementEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RuntimeCallTreeMeasurementEntityDTO result = service.save(runtimeCallTreeMeasurementEntityDTO);
        return ResponseEntity
            .created(new URI("/api/v2/measurements/runtime/calltrees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RuntimeCallTreeMeasurementEntityDTO>> bulkAddCallTreeMeasurementEntities(@RequestBody List<RuntimeCallTreeMeasurementEntityDTO> runtimeCallTreeMeasurementEntityDTOList) {
        log.debug("REST request to bulkAddCallTreeMeasurementEntities : {}", runtimeCallTreeMeasurementEntityDTOList);
        List<RuntimeCallTreeMeasurementEntityDTO> result = service.bulkAdd(runtimeCallTreeMeasurementEntityDTOList);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RuntimeCallTreeMeasurementEntityDTO> updateCallTreeMeasurementEntity(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO
    ) {
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
    ) {
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
    public ResponseEntity<List<RuntimeCallTreeMeasurementEntityDTO>> getAllCallTreeMeasurementEntities() {
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

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate/commit/{commitSha}}
     * Aggregate measurements by callstack for a specific commit
     *
     * @param commitSha the commit SHA to filter by
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated measurements in body
     */
    @GetMapping("/aggregate/commit/{commitSha}")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementByIterationDTO>> aggregateByCallstackForCommit(@PathVariable String commitSha) {
        log.debug("REST request to aggregate CallTreeMeasurements by callstack for commit {}", commitSha);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForCommit(commitSha);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate/repository/{repositoryName}}
     * Aggregate measurements by callstack for a specific repository
     *
     * @param repoName the repository name to filter by
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated measurements in body
     */
    @GetMapping("/aggregate/repository/{repoName}")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementByIterationDTO>> aggregateByCallstackForRepository(@PathVariable String repoName) {
        log.debug("REST request to aggregate CallTreeMeasurements by callstack for repository {}", repoName);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForRepository(repoName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate}
     * Aggregate measurements by callstack without any filter
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated measurements in body
     */
    @GetMapping("/aggregate")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementByIterationDTO>> aggregateByCallstack() {
        log.debug("REST request to aggregate CallTreeMeasurements by callstack without filter");
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate/normalized}}
     * Aggregate measurements by callstack and normalize time series to a fixed grid.
     * Each iteration's time series is resampled to the same time grid for comparison.
     *
     * @param gridPoints Optional: number of points in the target time grid (default: 100, max: 500)
     * @param limit Optional: maximum number of call stacks to return (default: 100, max: 500)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated + resampled measurements
     */
    @GetMapping("/aggregate/normalized")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementDTO>> aggregateByCallstackNormalized(
            @RequestParam(name = "gridPoints", required = false, defaultValue = "100") int gridPoints,
            @RequestParam(name = "limit", required = false, defaultValue = "100") int limit) {
        validateResponseSizeParameters(gridPoints, limit);
        log.debug("REST request to aggregate and normalize CallTreeMeasurements by callstack (gridPoints={}, limit={})", gridPoints, limit);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstack();
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> limitedResult = result.size() > limit ? result.subList(0, limit) : result;
        List<AggregatedRuntimeCallTreeMeasurementDTO> normalizedResult = postProcessor.processAggregations(limitedResult, gridPoints);
        return new ResponseEntity<>(normalizedResult, HttpStatus.OK);
    }

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate/commit/{commitSha}/normalized}}
     * Aggregate measurements by callstack for a specific commit and normalize time series.
     *
     * @param commitSha the commit SHA to filter by
     * @param gridPoints Optional: number of points in the target time grid (default: 100, max: 500)
     * @param limit Optional: maximum number of call stacks to return (default: 200, max: 500)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated + resampled measurements
     */
    @GetMapping("/aggregate/commit/{commitSha}/normalized")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementDTO>> aggregateByCallstackForCommitNormalized(
            @PathVariable String commitSha,
            @RequestParam(name = "gridPoints", required = false, defaultValue = "100") int gridPoints,
            @RequestParam(name = "limit", required = false, defaultValue = "200") int limit) {
        validateResponseSizeParameters(gridPoints, limit);
        log.debug("REST request to aggregate and normalize CallTreeMeasurements for commit {} (gridPoints={}, limit={})", commitSha, gridPoints, limit);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForCommit(commitSha);
        log.info("result 1 : {}", result.get(0));
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> limitedResult = result.size() > limit ? result.subList(0, limit) : result;
        List<AggregatedRuntimeCallTreeMeasurementDTO> normalizedResult = postProcessor.processAggregations(limitedResult, gridPoints);
        return new ResponseEntity<>(normalizedResult, HttpStatus.OK);
    }

    /**
     * {@code GET /api/v2/measurements/runtime/calltrees/aggregate/repository/{repositoryName}/normalized}}
     * Aggregate measurements by callstack for a specific repository and normalize time series.
     *
     * @param repoName the repository name to filter by
     * @param gridPoints Optional: number of points in the target time grid (default: 100, max: 500)
     * @param limit Optional: maximum number of call stacks to return (default: 200, max: 500)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and aggregated + resampled measurements
     */
    @GetMapping("/aggregate/repository/{repoName}/normalized")
    public ResponseEntity<List<AggregatedRuntimeCallTreeMeasurementDTO>> aggregateByCallstackForRepositoryNormalized(
            @PathVariable String repoName,
            @RequestParam(name = "gridPoints", required = false, defaultValue = "100") int gridPoints,
            @RequestParam(name = "limit", required = false, defaultValue = "200") int limit) {
        validateResponseSizeParameters(gridPoints, limit);
        log.debug("REST request to aggregate and normalize CallTreeMeasurements for repository {} (gridPoints={}, limit={})", repoName, gridPoints, limit);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> result = service.aggregateByCallstackForRepository(repoName);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> limitedResult = result.size() > limit ? result.subList(0, limit) : result;
        List<AggregatedRuntimeCallTreeMeasurementDTO> normalizedResult = postProcessor.processAggregations(limitedResult, gridPoints);
        return new ResponseEntity<>(normalizedResult, HttpStatus.OK);
    }

    /**
     * Validates request parameters to prevent excessive response sizes.
     *
     * @param gridPoints number of points in the target time grid
     * @param limit maximum number of call stacks to return
     * @throws BadRequestAlertException if parameters exceed safe limits
     */
    private void validateResponseSizeParameters(int gridPoints, int limit) {
        if (gridPoints <= 0 || gridPoints > 500) {
            throw new BadRequestAlertException("gridPoints must be between 1 and 500", ENTITY_NAME, "invalid_grid_points");
        }
        if (limit <= 0 || limit > 500) {
            throw new BadRequestAlertException("limit must be between 1 and 500", ENTITY_NAME, "invalid_limit");
        }
    }
}
