package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationRuntimeMeasurementsDTO;
import com.snail.sentinel.backend.service.dto.runtime.RuntimeValuesDTO;
import com.snail.sentinel.backend.service.dto.runtime.TimestampsDTO;
import com.snail.sentinel.backend.service.mapper.RuntimeCallTreeMeasurementEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuntimeCallTreeMeasurementServiceImpl implements RuntimeCallTreeMeasurementService {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeMeasurementServiceImpl.class);
    private final RuntimeCallTreeMeasurementRepository repository;
    private final RuntimeCallTreeMeasurementEntityMapper mapper;

    public RuntimeCallTreeMeasurementServiceImpl(RuntimeCallTreeMeasurementRepository repository, RuntimeCallTreeMeasurementEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RuntimeCallTreeMeasurementEntityDTO save(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to save CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        RuntimeCallTreeMeasurementEntity entity = mapper.toEntity(runtimeCallTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public List<RuntimeCallTreeMeasurementEntityDTO> bulkAdd(List<RuntimeCallTreeMeasurementEntityDTO> runtimeCallTreeMeasurementEntityDTOList) {
        List<RuntimeCallTreeMeasurementEntity> entities = mapper.toEntity(runtimeCallTreeMeasurementEntityDTOList);
        entities = repository.insert(entities);
        log.info("{} size CallTreeMeasurementEntity list inserted to the DB", entities.size());
        return mapper.toDto(entities);
    }

    @Override
    public RuntimeCallTreeMeasurementEntityDTO update(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to update CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        RuntimeCallTreeMeasurementEntity entity = mapper.toEntity(runtimeCallTreeMeasurementEntityDTO);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public Optional<RuntimeCallTreeMeasurementEntityDTO> partialUpdate(RuntimeCallTreeMeasurementEntityDTO runtimeCallTreeMeasurementEntityDTO) {
        log.debug("Request to partial update CallTreeMeasurementEntity : {}", runtimeCallTreeMeasurementEntityDTO);
        return repository
            .findById(runtimeCallTreeMeasurementEntityDTO.getId())
            .map(existingEntity -> {
                mapper.partialUpdate(existingEntity, runtimeCallTreeMeasurementEntityDTO);
                return existingEntity;
            })
            .map(repository::save)
            .map(mapper::toDto);
    }

    @Override
    public List<RuntimeCallTreeMeasurementEntityDTO> findAll() {
        log.debug("Request to get all CallTreeMeasurementEntities");
        return repository
            .findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @Override
    public Optional<RuntimeCallTreeMeasurementEntityDTO> findOne(String id) {
        log.debug("Request to get CallTreeMeasurementEntity : {}", id);
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CallTreeMeasurementEntity : {}", id);
        repository.deleteById(id);
    }

    /**
     * Aggregates all measurements by callstack
     * @return A list of aggregated measurements, where each entry represents a unique callstack and contains aggregated data for that callstack
     */
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack() {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack");
        return repository.aggregateByCallstack();
    }

    /**
     * Aggregates measurements by callstack for a specific commit SHA
     * @param commitSha commit sha used to filter the measurements before aggregation. Only measurements associated with this commit sha will be included in the aggregation
     * @return A list of aggregated measurements, where each entry represents a unique callstack and contains aggregated data for that callstack and a specific commit sha
     */
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackForCommit(String commitSha) {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack for commit {}", commitSha);
        return repository.aggregateByCallstackAndCommitSha(commitSha);
    }

    /**
     * Aggregates measurements by callstack for a specific repository
     * @param repoName Name of the repository
     * @return A list of aggregated measurements
     */
    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackForRepository(String repoName) {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack for repository {}", repoName);
        return repository.aggregateByCallstackAndRepositoryName(repoName);
    }

    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack(MeasurementAggregationFilter filter) {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack with filter type {}", filter.getFilterType());
        return repository.aggregateByCallstack(filter);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstack() {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack");
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstack();
        return aggregateAcrossIterations(byIteration);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForCommit(String commitSha) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for commit {}", commitSha);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForCommit(commitSha);
        return aggregateAcrossIterations(byIteration);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForRepository(String repoName) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for repository {}", repoName);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForRepository(repoName);
        return aggregateAcrossIterations(byIteration);
    }

    /**
     * Aggregates measurements across all iterations for each callstack
     * @param byIteration List of measurements aggregated by iteration
     * @return List of aggregated measurements across all iterations
     */
    private List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterations(List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration) {
        // Group by callstack to aggregate across iterations
        Map<String, List<AggregatedRuntimeCallTreeMeasurementByIterationDTO>> groupedByCallstack = byIteration.stream()
            .collect(Collectors.groupingBy(m -> String.join(".", m.getCallstack())));

        return groupedByCallstack.values().stream()
            .map(measurements -> {
                if (measurements.isEmpty()) {
                    return null;
                }

                // Get the first measurement to extract common properties
                AggregatedRuntimeCallTreeMeasurementByIterationDTO firstMeasurement = measurements.get(0);

                // Create the aggregated DTO
                AggregatedRuntimeCallTreeMeasurementDTO aggregated = new AggregatedRuntimeCallTreeMeasurementDTO();
                aggregated.setCallstack(firstMeasurement.getCallstack());
                aggregated.setScope(firstMeasurement.getScope());
                aggregated.setType(firstMeasurement.getType());
                aggregated.setCommit(firstMeasurement.getCommit());

                // Aggregate measurements across iterations
                IterationRuntimeMeasurementsDTO aggregatedMeasurements = aggregateMeasurements(measurements);
                aggregated.setMeasurements(aggregatedMeasurements);

                return aggregated;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Aggregates runtime measurements by combining values and timestamps from multiple iterations
     * @param measurements List of measurements for a single callstack across multiple iterations
     * @return Aggregated measurements
     */
    private IterationRuntimeMeasurementsDTO aggregateMeasurements(List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> measurements) {
        IterationRuntimeMeasurementsDTO result = new IterationRuntimeMeasurementsDTO();

        // Collect all values from all iterations
        List<Double> allValues = measurements.stream()
            .flatMap(m -> m.getValues().getValues().stream())
            .collect(Collectors.toList());

        // Collect all timestamps from all iterations
        List<Long> allTimestamps = measurements.stream()
            .flatMap(m -> m.getTimestamps().getTimestamps().stream())
            .collect(Collectors.toList());

        // Aggregate total energy
        Double totalEnergy = measurements.stream()
            .flatMap(m -> m.getValues().getValues().stream())
            .reduce(0.0, Double::sum);

        // Set the aggregated values
        RuntimeValuesDTO aggregatedValues = new RuntimeValuesDTO();
        aggregatedValues.setValues(allValues);
        result.setRuntimeValues(aggregatedValues);

        TimestampsDTO aggregatedTimestamps = new TimestampsDTO();
        aggregatedTimestamps.setTimestamps(allTimestamps);
        result.setTimestamps(aggregatedTimestamps);

        result.setTotalEnergy(totalEnergy);
        // Note: iteration field is set to null since we're aggregating across all iterations

        return result;
    }
}
