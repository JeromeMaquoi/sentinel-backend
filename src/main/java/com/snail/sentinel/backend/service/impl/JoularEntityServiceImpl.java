package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.JoularEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.CommitSimpleDTONotSetException;
import com.snail.sentinel.backend.service.exceptions.FileListProviderNotSetException;
import com.snail.sentinel.backend.service.exceptions.IterationDTONotSetException;
import com.snail.sentinel.backend.service.exceptions.NoCsvLineFoundException;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
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

    private static final String CLASS_NAME = "className";

    private static final String METHOD_NAME = "methodName";

    private static final String LINE_NUMBER = "lineNumber";

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
        return joularEntityRepository.findByCommitSha(sha);
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
    public void handleJoularEntityCreationForOneIteration(Path iterationFilePath, CommitSimpleDTO commitSimpleDTO, IterationDTO iterationDTO, FileListProvider fileListProvider, CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO) {
        log.info("Request to handle JoularEntity for iteration {}", iterationFilePath);
        joularResourceService.setCommitSimpleDTO(commitSimpleDTO);
        joularResourceService.setIterationDTO(iterationDTO);
        joularResourceService.setFileListProvider(fileListProvider);
        joularResourceService.setCkAggregateLineHashMapDTO(ckAggregateLineHashMapDTO);
        JoularEntityListDTO joularEntityListDTO = createJoularEntityDTOList(iterationFilePath);
        log.info("joularEntityListDTO created!");
        insertJoularData(joularEntityListDTO);
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
        Path csvPath = joularResourceService.getFileListProvider().getFileList(iterationFilePath + "/app/total/methods").iterator().next();
        try {
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvPath.toString());
            for (JSONObject line : allLines) {
                handleOneCsvLine(line);
            }
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
        return joularResourceService.getJoularEntityListDTO();
    }

    public void handleOneCsvLine(JSONObject line) {
        String nextLine = line.keySet().iterator().next();
        String regex = "^([+-]?\\d*\\.?\\d*)$";
        if (Pattern.matches(regex, line.getString(nextLine))) {
            Float value = line.getFloat(nextLine);
            JSONObject classMethodLine = getClassMethodLine(nextLine);
            if (classMethodLine != null) {
                CkAggregateLineDTO matchedCkJoular = getMatchCkJoular(classMethodLine);
                if (matchedCkJoular != null) {
                    String classMethodSignature = getClassMethodSignature(nextLine);
                    MeasurableElementDTO methodElementDTO = getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);
                    addOrUpdateJoularEntityListDTO(methodElementDTO, value);
                }
            }
        }
    }

    public void addOrUpdateJoularEntityListDTO(MeasurableElementDTO methodElementDTO, Float value) {
        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        if (joularResourceService.getMethodElementSetDTO().has(methodElementDTO)) {
            joularResourceService.getJoularEntityListDTO().update(methodElementDTO, value);
        } else {
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

    public void insertJoularData(JoularEntityListDTO joularEntityListDTO) {
        bulkAdd(joularEntityListDTO.getList());
        log.info("List of JoularEntity inserted to the database");
    }

    public String getClassMethodSignature(String line) {
        return line.substring(0, line.lastIndexOf(" "));
    }

    public CkAggregateLineDTO getMatchCkJoular(JSONObject classMethodLine) {
        String className = Util.classNameParser(classMethodLine.getString(CLASS_NAME));
        String methodName = Util.methodNameParser(className, classMethodLine.getString(METHOD_NAME));
        int numberLine = classMethodLine.getInt(LINE_NUMBER);
        if (numberLine > 0) {
            List<CkAggregateLineDTO> allOccurrences = joularResourceService.getCkAggregateLineHashMapDTO().getAllOccurrences(className, methodName);
            //log.debug("allOccurrences : {}", allOccurrences);
            if (!allOccurrences.isEmpty()) {
                for (CkAggregateLineDTO occ : allOccurrences) {
                    //log.debug("{} <= {} && ({} + {}) >= {} ?", occ.getLine(), numberLine, occ.getLine(), occ.getLoc(), numberLine);
                    if (occ.getLine() <= numberLine && (occ.getLine() + occ.getLoc()) >= numberLine) {
                        //log.debug("Occurrence returned for \"{}.{}\" at line {}", className, methodName, numberLine);
                        return occ;
                    }
                }
            } else if (!methodName.contains("access$")) {
                //log.debug("No occurrence for {} {} at line {}", className, methodName, numberLine);
            }
        } /*else {
            log.warn("The number of line is negative for \"{}.{}\"", className, methodName);
        }*/
        return null;
    }

    public IterationDTO createIterationDTOFromCsvFileName(String fileName) {
        Integer iterationId = parseInt(fileName.split("-")[0]);
        Integer pid = parseInt(fileName.split("-")[1]);
        long startTimestamp = parseLong(fileName.split("-")[2]);
        return new IterationDTO(iterationId, pid, startTimestamp);
    }

    public JSONObject getClassMethodLine(String metric) {
        String className = metric.substring(0, metric.lastIndexOf('.'));
        String[] spaceSplit = metric.substring(metric.lastIndexOf('.') + 1).split(" ");
        if (spaceSplit.length < 3) {
            String methodName = spaceSplit[0];
            int numberLine = parseInt(spaceSplit[1]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_NAME, className);
            jsonObject.put(METHOD_NAME, methodName);
            jsonObject.put(LINE_NUMBER, numberLine);
            return jsonObject;
        }
        return null;
    }
}
