package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.JoularNodeEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
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

    private JoularNodeEntity joularNodeEntity;

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
    public void handleJoularNodeEntityCreationForOneIteration(Path iterationFilePath) {
        log.info("Request to handle JoularNodeEntity for iteration {}", iterationFilePath);
        JoularNodeEntityListDTO joularNodeEntityListDTO = createJoularNodeEntityDTOList(iterationFilePath);
        //bulkAdd(joularNodeEntityListDTO.getList());
    }

    public JoularNodeEntityListDTO createJoularNodeEntityDTOList(Path iterationFilePath) {
        log.debug("iterationFilePath : {}", iterationFilePath);
        joularResourceService.setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());
        Path csvpath = joularResourceService.getFileListProvider().getFileList(iterationFilePath + "/app/total/calltrees").iterator().next();
        try {
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvpath.toString());
            for (JSONObject line : allLines) {
                handleOneNodeCsvLine(line);
            }
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }

        return null;
    }

    public void handleOneNodeCsvLine(JSONObject line) {
        String nextLine = line.keySet().iterator().next();
        Float value = line.getFloat(nextLine);
        String[] allLineNodes = getEachNodeFromStringLine(nextLine);

        // List of all ancestors of one method of the line
        joularResourceService.setAncestors(new ArrayList<>());
        for (String methodNameAndLine : allLineNodes) {
            handleOneMethod(methodNameAndLine);
        }
    }

    public void handleOneMethod(String methodNameAndLine) {
        log.debug("handleOneMethod with {}", methodNameAndLine);
        Integer lineNumber = Integer.valueOf(methodNameAndLine.split(" ")[1]);
        createBaseJoularNodeEntity(lineNumber);
        updateJoularNodeEntityMeasurableElement(methodNameAndLine);
        joularResourceService.getAncestors().add(joularNodeEntity.getId());
    }

    public void createBaseJoularNodeEntity(Integer lineNumber) {
        joularNodeEntity = new JoularNodeEntity();
        joularNodeEntity.setId(UUID.randomUUID().toString());
        joularNodeEntity.setLineNumber(lineNumber);
        joularNodeEntity.setScope("app");
        joularNodeEntity.setMonitoringType("calltrees");
        joularNodeEntity.setIteration(joularResourceService.getIterationDTO());
        joularNodeEntity.setCommit(joularResourceService.getCommitSimpleDTO());
        joularNodeEntity.setAncestors(joularResourceService.getAncestors());
    }

    public void updateJoularNodeEntityMeasurableElement(String classMethodLineString) {
        Optional<CkAggregateLineDTO> optionalMatchedCkJoular = joularResourceService.getMatchCkJoular(classMethodLineString);
        if (optionalMatchedCkJoular.isPresent()) {
            CkAggregateLineDTO matchedCkJoular = optionalMatchedCkJoular.get();
            String classMethodSignature = classMethodLineString.substring(0, classMethodLineString.lastIndexOf(" "));
            MeasurableElementDTO methodElementDTO = Util.getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);
            joularNodeEntity.setMeasurableElement(methodElementDTO);
        }
        //TODO add parent field!!!
        log.info("joularNodeEntity : {}", joularNodeEntity);
    }

    public String[] getEachNodeFromStringLine(String line) {
        return line.split(";");
    }
}
