package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.JoularEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.*;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementSetDTO;
import com.snail.sentinel.backend.service.mapper.JoularEntityMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

import static com.snail.sentinel.backend.commons.Util.getMeasurableElementForJoular;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Service
public class JoularEntityServiceImpl implements JoularEntityService {
    private final Logger log = LoggerFactory.getLogger(JoularEntityServiceImpl.class);

    private final JoularEntityRepository joularEntityRepository;

    private final JoularEntityMapper joularEntityMapper;

    private final JoularResourceService joularResourceService;

    private int numberOfMethods;

    private int numberOfUnhandledMethods;

    public JoularEntityServiceImpl(JoularEntityRepository joularEntityRepository, JoularEntityMapper joularEntityMapper, JoularResourceService joularResourceService) {
        this.joularEntityRepository = joularEntityRepository;
        this.joularEntityMapper = joularEntityMapper;
        this.joularResourceService = joularResourceService;
    }

    @Override
    public List<JoularEntityDTO> findAll() {
        return joularEntityRepository.findAll().stream().map(joularEntityMapper::toDto).toList();
    }

    @Override
    public List<JoularEntity> findByCommitSha(String sha) {
        log.info("Find JoularEntities by commit sha : {}", sha);
        return joularEntityRepository.findByCommitSha(sha);
    }

    @Override
    public int countByCommitSha(String sha) {
        log.info("Count number of JoularEntities for sha : {} = {}", sha, joularEntityRepository.countByCommitSha(sha));
        return joularEntityRepository.countByCommitSha(sha);
    }

    @Override
    public List<JoularEntity> findByCommitShaAndAstElement(String sha, String className, String classMethodSignature) {
        return joularEntityRepository.findByCommitShaAndMeasurableElementClassNameAndMeasurableElementClassMethodSignature(sha, className, classMethodSignature);
    }

    @Override
    public List<JoularEntityDTO> bulkAdd(List<JoularEntityDTO> listJoular) {
        List<JoularEntity> listEntity = joularEntityMapper.toEntity(listJoular);
        listEntity = joularEntityRepository.insert(listEntity);
        log.info("{} size JoularEntity list inserted to the DB", listEntity.size());
        return joularEntityMapper.toDto(listEntity);
    }

    @Override
    public void deleteAll() {
        joularEntityRepository.deleteAll();
    }

    @Override
    public void deleteByCommitSha(String commitSha) {
        joularEntityRepository.deleteByCommitSha(commitSha);
    }

    @Override
    public void handleJoularEntityCreationForOneIteration(Path iterationFilePath) {
        log.info("Request to handle JoularEntity for iteration {}", iterationFilePath);
        setNumberOfMethods(0);
        setNumberOfUnhandledMethods(0);
        JoularEntityListDTO joularEntityListDTO = createJoularEntityDTOList(iterationFilePath);
        insertJoularEntityListData(joularEntityListDTO);
    }

    @Override
    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int numberOfMethods) {
        this.numberOfMethods = numberOfMethods;
    }

    public void addMethod(){
        numberOfMethods++;
    }

    @Override
    public int getNumberOfUnhandledMethods() {
        return numberOfUnhandledMethods;
    }

    public void setNumberOfUnhandledMethods(int numberOfUnhandledMethods) {
        this.numberOfUnhandledMethods = numberOfUnhandledMethods;
    }

    public void addUnhandledMethod() {
        numberOfUnhandledMethods++;
    }

    public JoularEntityListDTO createJoularEntityDTOList(Path iterationFilePath) {
        if (joularResourceService.getFileListProvider() == null) {
            throw new FileListProviderNotSetException("createJoularEntityDTOList");
        }
        if (joularResourceService.getCommitSimpleDTO() == null) {
            throw new CommitSimpleDTONotSetException("createJoularEntityDTOList");
        }
        if (joularResourceService.getIterationDTO() == null) {
            throw new IterationDTONotSetException("createJoularEntityDTOList");
        }
        joularResourceService.setJoularEntityListDTO(new JoularEntityListDTO());
        joularResourceService.setMethodElementSetDTO(new MethodElementSetDTO());
        try {
            Path csvPath = joularResourceService.getFileListProvider().getFileList(iterationFilePath + "/app/total/methods").iterator().next();
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvPath.toString());
            log.info("Creation of the JoularEntityListDTO");
            for (JSONObject line : allLines) {
                handleOneCsvLine(line);
            }
        } catch (NoSuchElementException e) {
            log.warn("No file found for the given iteration path: {}", iterationFilePath);
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
        return joularResourceService.getJoularEntityListDTO();
    }

    public void handleOneCsvLine(JSONObject line) {
        String nextLine = line.keySet().iterator().next();
        String regex = "^([+-]?\\d*\\.?\\d*)$";
        addMethod();
        if (Pattern.matches(regex, line.getString(nextLine))) {
            log.debug("line : {}", nextLine);
            Float value = line.getFloat(nextLine);
            Optional<CkAggregateLineDTO> optionalResult = joularResourceService.getMatchCkJoular(nextLine);
            if (optionalResult.isPresent()) {
                CkAggregateLineDTO matchedCkJoular = optionalResult.orElseThrow(() -> new GetMatchCkJoularException(nextLine));
                String classMethodSignature = getClassMethodSignature(nextLine);
                MeasurableElementDTO methodElementDTO = getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);
                addOrUpdateJoularEntityListDTO(methodElementDTO, value);
            } else {
                //joularResourceService.getLoggingToFile().writeTimeToFileForWarningIterationResult(getNumberOfMethods(), "No JoularEntity set for " + nextLine);
                addUnhandledMethod();
            }
        }
    }

    public void addOrUpdateJoularEntityListDTO(MeasurableElementDTO methodElementDTO, Float value) {
        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        if (joularResourceService.getMethodElementSetDTO().has(methodElementDTO)) {
            log.debug("Updating JoularNodeEntityDTO from list");
            joularResourceService.getJoularEntityListDTO().update(methodElementDTO, value);
        } else {
            log.debug("Adding JoularNodeEntityDTO to list");
            joularResourceService.getMethodElementSetDTO().add(methodElementDTO);
            joularEntityDTO.setIterationDTO(joularResourceService.getIterationDTO());
            joularEntityDTO.setCommitSimpleDTO(joularResourceService.getCommitSimpleDTO());
            joularEntityDTO.setScope("app");
            joularEntityDTO.setMonitoringType("total");
            joularEntityDTO.setValue(value);
            joularEntityDTO.setMethodElementDTO(methodElementDTO);
            //log.debug("New : {}", joularEntityDTO.getMethodElementDTO().getClassMethodSignature());
            joularResourceService.getJoularEntityListDTO().add(joularEntityDTO);
        }
    }

    public void insertJoularEntityListData(JoularEntityListDTO joularEntityListDTO) {
        bulkAdd(joularEntityListDTO.getList());
        log.info("List of JoularEntity inserted to the database");
    }

    public String getClassMethodSignature(String line) {
        return line.substring(0, line.lastIndexOf(" "));
    }

    public IterationDTO createIterationDTOFromCsvFileName(String fileName) {
        Integer iterationId = parseInt(fileName.split("-")[0]);
        Integer pid = parseInt(fileName.split("-")[1]);
        long startTimestamp = parseLong(fileName.split("-")[2]);
        return new IterationDTO(iterationId, pid, startTimestamp);
    }
}
