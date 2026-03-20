package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.domain.RuntimeCallTreeMeasurementEntity;
import com.snail.sentinel.backend.repository.RuntimeCallTreeMeasurementRepository;
import com.snail.sentinel.backend.repository.filter.MeasurementAggregationFilter;
import com.snail.sentinel.backend.service.ConstructorCallstackMatcher;
import com.snail.sentinel.backend.service.RuntimeCallTreeMeasurementService;
import com.snail.sentinel.backend.service.dto.RuntimeCallTreeMeasurementEntityDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementByIterationDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeWithConstructorsDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationRuntimeMeasurementsDTO;
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
    private final ConstructorCallstackMatcher constructorCallstackMatcher;

    public RuntimeCallTreeMeasurementServiceImpl(
            RuntimeCallTreeMeasurementRepository repository,
            RuntimeCallTreeMeasurementEntityMapper mapper,
            ConstructorCallstackMatcher constructorCallstackMatcher) {
        this.repository = repository;
        this.mapper = mapper;
        this.constructorCallstackMatcher = constructorCallstackMatcher;
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

    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstack() {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack");
        return repository.aggregateByCallstack();
    }

    public List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> aggregateByCallstackForCommit(String commitSha) {
        log.debug("Service request to aggregate CallTreeMeasurements by callstack for commit {}", commitSha);
        return repository.aggregateByCallstackAndCommitSha(commitSha);
    }

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
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstack(Integer minIterations) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack with minIterations={}", minIterations);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstack();
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterations(byIteration);
        return filterByMinIterations(aggregated, minIterations);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForCommit(String commitSha) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for commit {}", commitSha);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForCommit(commitSha);
        return aggregateAcrossIterations(byIteration);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForCommit(String commitSha, Integer minIterations) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for commit {} with minIterations={}",
            commitSha, minIterations);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForCommit(commitSha);
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterations(byIteration);
        return filterByMinIterations(aggregated, minIterations);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForRepository(String repoName) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for repository {}", repoName);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForRepository(repoName);
        return aggregateAcrossIterations(byIteration);
    }

    @Override
    public List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterationsByCallstackForRepository(String repoName, Integer minIterations) {
        log.debug("Service request to aggregate CallTreeMeasurements across all iterations by callstack for repository {} with minIterations={}",
            repoName, minIterations);
        List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration = aggregateByCallstackForRepository(repoName);
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterations(byIteration);
        return filterByMinIterations(aggregated, minIterations);
    }

    private List<AggregatedRuntimeCallTreeMeasurementDTO> aggregateAcrossIterations(List<AggregatedRuntimeCallTreeMeasurementByIterationDTO> byIteration) {
        Map<String, List<AggregatedRuntimeCallTreeMeasurementByIterationDTO>> groupedByCallstack = byIteration.stream()
            .collect(Collectors.groupingBy(m -> String.join(".", m.getCallstack())));

        return groupedByCallstack.values().stream()
            .map(measurements -> {
                if (measurements.isEmpty()) {
                    return null;
                }

                AggregatedRuntimeCallTreeMeasurementByIterationDTO firstMeasurement = measurements.get(0);

                AggregatedRuntimeCallTreeMeasurementDTO aggregated = new AggregatedRuntimeCallTreeMeasurementDTO();
                aggregated.setCallstack(firstMeasurement.getCallstack());
                aggregated.setScope(firstMeasurement.getScope());
                aggregated.setType(firstMeasurement.getType());
                aggregated.setCommit(firstMeasurement.getCommit());

                List<IterationRuntimeMeasurementsDTO> iterationMeasurements = measurements.stream()
                    .map(this::createIterationMeasurement)
                    .toList();
                aggregated.setMeasurements(iterationMeasurements);

                return aggregated;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    private IterationRuntimeMeasurementsDTO createIterationMeasurement(AggregatedRuntimeCallTreeMeasurementByIterationDTO measurement) {
        IterationRuntimeMeasurementsDTO result = new IterationRuntimeMeasurementsDTO();
        result.setIteration(measurement.getIteration());

        if (measurement.getValues() != null) {
            result.setRuntimeValues(measurement.getValues());
        } else {
            result.setRuntimeValues(Collections.emptyList());
        }

        if (measurement.getTimestamps() != null) {
            result.setTimestamps(measurement.getTimestamps());
        } else {
            result.setTimestamps(Collections.emptyList());
        }

        Double totalEnergy = 0.0;
        if (measurement.getValues() != null && !measurement.getValues().isEmpty()) {
            totalEnergy = measurement.getValues().stream()
                .reduce(0.0, Double::sum);
        }
        result.setTotalEnergy(totalEnergy);

        return result;
    }

    private List<AggregatedRuntimeCallTreeMeasurementDTO> filterByMinIterations(
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregates,
        Integer minIterations) {

        if (minIterations == null) {
            return aggregates;
        }

        return aggregates.stream()
            .filter(agg -> {
                int measurementCount = agg.getMeasurements() != null ? agg.getMeasurements().size() : 0;
                return measurementCount >= minIterations;
            })
            .toList();
    }

    @Override
    public List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacks(Integer minIterations) {
        log.debug("Service request to find constructors in aggregated CallTreeMeasurements with minIterations={}", minIterations);
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterationsByCallstack(minIterations);
        return enrichWithConstructors(aggregated);
    }

    @Override
    public List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacksForCommit(String commitSha, Integer minIterations) {
        log.debug("Service request to find constructors in aggregated CallTreeMeasurements for commit {} with minIterations={}",
            commitSha, minIterations);
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterationsByCallstackForCommit(commitSha, minIterations);
        return enrichWithConstructors(aggregated);
    }

    @Override
    public List<AggregatedRuntimeCallTreeWithConstructorsDTO> findConstructorsInAggregatedCallstacksForRepository(String repoName, Integer minIterations) {
        log.debug("Service request to find constructors in aggregated CallTreeMeasurements for repository {} with minIterations={}",
            repoName, minIterations);
        List<AggregatedRuntimeCallTreeMeasurementDTO> aggregated = aggregateAcrossIterationsByCallstackForRepository(repoName, minIterations);
        return enrichWithConstructors(aggregated);
    }

    private List<AggregatedRuntimeCallTreeWithConstructorsDTO> enrichWithConstructors(
            List<AggregatedRuntimeCallTreeMeasurementDTO> aggregatedMeasurements) {

        return aggregatedMeasurements.stream()
            .map(measurement -> {
                var matchedConstructors = constructorCallstackMatcher.findMatchingConstructors(measurement);
                return new AggregatedRuntimeCallTreeWithConstructorsDTO(measurement, matchedConstructors);
            })
            .toList();
    }
}
