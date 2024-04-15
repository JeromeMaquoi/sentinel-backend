package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.JoularNodeEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
import com.snail.sentinel.backend.service.dto.joularNode.JoularNodeHashMapDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.NoCsvLineFoundException;
import com.snail.sentinel.backend.service.mapper.JoularNodeEntityMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing {@link com.snail.sentinel.backend.domain.JoularNodeEntity}.
 */
@Service
public class JoularNodeEntityServiceImpl implements JoularNodeEntityService {

    private final Logger log = LoggerFactory.getLogger(JoularNodeEntityServiceImpl.class);

    private final JoularNodeEntityRepository joularNodeEntityRepository;

    private final JoularNodeEntityMapper joularNodeEntityMapper;

    private final JoularResourceService joularResourceService;

    private JoularNodeEntityDTO joularNodeEntityDTO;

    private boolean isLastElement;

    private JoularNodeHashMapDTO joularNodeHashMapDTO;

    public JoularNodeEntityServiceImpl(
            JoularNodeEntityRepository joularNodeEntityRepository,
            JoularNodeEntityMapper joularNodeEntityMapper,
            JoularResourceService joularResourceService
    ) {
        this.joularNodeEntityRepository = joularNodeEntityRepository;
        this.joularNodeEntityMapper = joularNodeEntityMapper;
        this.joularResourceService = joularResourceService;
    }

    @Override
    public JoularNodeEntityDTO save(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to save JoularNodeEntity : {}", joularNodeEntityDTO);
        JoularNodeEntity joularNodeEntity = joularNodeEntityMapper.toEntity(joularNodeEntityDTO);
        joularNodeEntity = joularNodeEntityRepository.save(joularNodeEntity);
        return joularNodeEntityMapper.toDto(joularNodeEntity);
    }

    @Override
    public List<JoularNodeEntityDTO> bulkAdd(List<JoularNodeEntityDTO> joularNodeEntityDTOList) {
        List<JoularNodeEntity> listEntity = joularNodeEntityMapper.toEntity(joularNodeEntityDTOList);
        listEntity = joularNodeEntityRepository.insert(listEntity);
        log.info("{} size JoularNodeEntity list inserted to the DB", listEntity.size());
        return joularNodeEntityMapper.toDto(listEntity);
    }

    @Override
    public JoularNodeEntityDTO update(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to update JoularNodeEntity : {}", joularNodeEntityDTO);
        JoularNodeEntity joularNodeEntity = joularNodeEntityMapper.toEntity(joularNodeEntityDTO);
        joularNodeEntity = joularNodeEntityRepository.save(joularNodeEntity);
        return joularNodeEntityMapper.toDto(joularNodeEntity);
    }

    @Override
    public Optional<JoularNodeEntityDTO> partialUpdate(JoularNodeEntityDTO joularNodeEntityDTO) {
        log.debug("Request to partially update JoularNodeEntity : {}", joularNodeEntityDTO);

        return joularNodeEntityRepository
            .findById(joularNodeEntityDTO.getId())
            .map(existingJoularNodeEntity -> {
                joularNodeEntityMapper.partialUpdate(existingJoularNodeEntity, joularNodeEntityDTO);

                return existingJoularNodeEntity;
            })
            .map(joularNodeEntityRepository::save)
            .map(joularNodeEntityMapper::toDto);
    }

    @Override
    public List<JoularNodeEntityDTO> findAll() {
        log.debug("Request to get all JoularNodeEntities");
        return joularNodeEntityRepository
            .findAll()
            .stream()
            .map(joularNodeEntityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<JoularNodeEntityDTO> findOne(String id) {
        log.debug("Request to get JoularNodeEntity : {}", id);
        return joularNodeEntityRepository.findById(id).map(joularNodeEntityMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete JoularNodeEntity : {}", id);
        joularNodeEntityRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        joularNodeEntityRepository.deleteAll();
    }

    @Override
    public void handleJoularNodeEntityCreationForOneIteration(Path iterationFilePath) {
        log.info("Request to handle JoularNodeEntity for iteration {}", iterationFilePath);
        this.joularNodeHashMapDTO = new JoularNodeHashMapDTO();
        createJoularNodeEntityDTOList(iterationFilePath);
        bulkAdd(joularResourceService.getJoularNodeEntityListDTO().getList());
    }

    public void createJoularNodeEntityDTOList(Path iterationFilePath) {
        log.debug("iterationFilePath : {}", iterationFilePath);
        joularResourceService.setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());
        Path csvpath = joularResourceService.getFileListProvider().getFileList(iterationFilePath + "/app/total/calltrees").iterator().next();
        try {
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvpath.toString());
            for (JSONObject line : allLines) {
                handleOneCsvLine(line);
            }
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
    }

    public void handleOneCsvLine(JSONObject line) {
        String nextLine = line.keySet().iterator().next();
        Float value = line.getFloat(nextLine);
        List<String> allLineNodes = getEachNodeFromStringLine(nextLine);
        // List of all ancestors of one method of the line
        joularResourceService.setAncestors(new ArrayList<>());
        // Check if last element of array
        for (String methodNameAndLine : allLineNodes) {
            setLastElement(allLineNodes.indexOf(methodNameAndLine) == allLineNodes.size() - 1);
            handleOneMethodFromOneCsvLine(methodNameAndLine, value);
        }
    }

    public void handleOneMethodFromOneCsvLine(String methodNameAndLine, Float value) {
        Optional<JoularNodeEntityDTO> optionalJoularNodeEntity = createJoularNodeEntityDTO(methodNameAndLine, value);
        if (optionalJoularNodeEntity.isPresent()) {
            JoularNodeEntityDTO joularNodeEntity = optionalJoularNodeEntity.get();
            joularResourceService.getAncestors().add(joularNodeEntity.getId());
            joularResourceService.getJoularNodeEntityListDTO().add(joularNodeEntity);
        }
    }

    public Optional<JoularNodeEntityDTO> createJoularNodeEntityDTO(String classMethodLineString, Float value) {
        int lineNumber = Integer.parseInt(classMethodLineString.split(" ")[1]);
        assert this.joularNodeHashMapDTO != null : "joularNodeHashMapDTO is null";
        if (this.joularNodeHashMapDTO.isJoularNodeEntityDTOInMap(classMethodLineString)) {
            joularNodeEntityDTO = this.joularNodeHashMapDTO.getJoularNodeEntityDTO(classMethodLineString);
        } else {
            Optional<MeasurableElementDTO> optionalMeasurableElementDTO = getMeasurableElementDTO(classMethodLineString, lineNumber);
            if (optionalMeasurableElementDTO.isPresent()) {
                joularNodeEntityDTO = new JoularNodeEntityDTO();
                joularNodeEntityDTO.setMeasurableElement(optionalMeasurableElementDTO.get());
                joularNodeEntityDTO.setId(UUID.randomUUID().toString());
                joularNodeEntityDTO.setLineNumber(lineNumber);
                joularNodeEntityDTO.setScope("app");
                joularNodeEntityDTO.setMonitoringType("calltrees");
                joularNodeEntityDTO.setIteration(joularResourceService.getIterationDTO());
                joularNodeEntityDTO.setCommit(joularResourceService.getCommitSimpleDTO());
                List<String> ancestors = new ArrayList<>(joularResourceService.getAncestors());
                joularNodeEntityDTO.setAncestors(ancestors);
                joularNodeEntityDTO.setParent(getParentFromAncestors());
                if (isLastElement()) {
                    joularNodeEntityDTO.setValue(value);
                }
                this.joularNodeHashMapDTO.insertOne(joularNodeEntityDTO);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(joularNodeEntityDTO);
    }

    public Optional<MeasurableElementDTO> getMeasurableElementDTO(String classMethodLineString, int lineNumber) {
        Optional<MeasurableElementDTO> optionalMeasurableElementDTO = createJoularNodeEntityMeasurableElement(classMethodLineString);
        if (optionalMeasurableElementDTO.isEmpty()) {
            if (lineNumber > 0 && !classMethodLineString.contains("$$Lambda$") && !classMethodLineString.contains("<clinit>") && !classMethodLineString.contains("<init>")) {
                log.error("MeasurableElement not set for JoularNodeEntity with classMethodLine : {} for iteration {} of project {}", classMethodLineString, joularResourceService.getIterationDTO().getIterationId(), joularResourceService.getCommitSimpleDTO().getRepository().getName());
            }
            return Optional.empty();
        }
        return optionalMeasurableElementDTO;
    }

    public String getParentFromAncestors() {
        if (!joularResourceService.getAncestors().isEmpty()) {
            List<String> ancestors = joularResourceService.getAncestors();
            return ancestors.get(ancestors.size()-1);
        }
        return null;
    }

    public Optional<MeasurableElementDTO> createJoularNodeEntityMeasurableElement(String classMethodLineString) {
        Optional<CkAggregateLineDTO> optionalMatchedCkJoular = joularResourceService.getMatchCkJoular(classMethodLineString);
        if (optionalMatchedCkJoular.isPresent()) {
            CkAggregateLineDTO matchedCkJoular = optionalMatchedCkJoular.get();
            String classMethodSignature = classMethodLineString.substring(0, classMethodLineString.lastIndexOf(" "));
            return Optional.of(Util.getMeasurableElementForJoular(matchedCkJoular, classMethodSignature));
        }
        return Optional.empty();
    }

    public List<String> getEachNodeFromStringLine(String line) {
        return Arrays.stream(line.split(";")).toList();
    }

    public boolean isLastElement() {
        return isLastElement;
    }

    public void setLastElement(boolean lastElement) {
        isLastElement = lastElement;
    }

    public void setJoularNodeHashMapDTO(JoularNodeHashMapDTO joularNodeHashMapDTO) {
        this.joularNodeHashMapDTO = joularNodeHashMapDTO;
    }
}
