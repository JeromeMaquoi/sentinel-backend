package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.repository.JoularEntityRepository;
import com.snail.sentinel.backend.service.JoularEntityService;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.NoCsvLineFoundException;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
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

    private static final String CLASS_NAME = "className";

    private static final String METHOD_NAME = "methodName";

    private static final String LINE_NUMBER = "lineNumber";

    public JoularEntityServiceImpl(JoularEntityRepository joularEntityRepository, JoularEntityMapper joularEntityMapper) {
        this.joularEntityRepository = joularEntityRepository;
        this.joularEntityMapper = joularEntityMapper;
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
    public JoularEntityListDTO createJoularEntityDTOList(CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO, CommitCompleteDTO commitCompleteDTO, String iterationPath) {
        log.info("Request to create JoularEntityDTO list for {}", iterationPath);
        JoularEntityListDTO joularEntityDTOList = new JoularEntityListDTO();
        Set<Path> fileList = Util.getFileList(iterationPath);
        log.debug("ckAggregateLineHashMapDTO size : {}", ckAggregateLineHashMapDTO.size());
        for (Path filePath: fileList) {
            JoularEntityListDTO iterationJoularDTOList = createJoularEntityDTOListForOneIteration(filePath, ckAggregateLineHashMapDTO, commitCompleteDTO);
            joularEntityDTOList.concat(joularEntityDTOList, iterationJoularDTOList);
        }
        return joularEntityDTOList;
    }

    private JoularEntityListDTO createJoularEntityDTOListForOneIteration(Path iterationDirPath, CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO, CommitCompleteDTO commitCompleteDTO) {
        JoularEntityListDTO joularEntityDTOList = new JoularEntityListDTO();
        MethodElementSetDTO methodElementSetDTO = new MethodElementSetDTO();

        String csvPathFileName = iterationDirPath.getFileName().toString();
        IterationDTO iterationDTO = createIterationDTOFromCsvFileName(csvPathFileName);
        Path csvPath = Util.getFileList(iterationDirPath + "/app/total/methods/").iterator().next();
        try {
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvPath.toString());
            int nbAfterPatternMatching = 0;
            int nbAfterClassMethodLine = 0;
            int nbAfterMatchedCkJoular = 0;
            for (JSONObject line: allLines) {
                String nextLine = line.keySet().iterator().next();
                log.debug("nextLine : {}", nextLine);
                String regex = "^([+-]?\\d*\\.?\\d*)$";
                if (Pattern.matches(regex, line.getString(nextLine))) {
                    nbAfterPatternMatching += 1;
                    Float value = line.getFloat(nextLine);
                    JSONObject classMethodLine = getClassMethodLine(nextLine);
                    //log.debug(String.valueOf(classMethodLine));
                    if (classMethodLine != null) {
                        nbAfterClassMethodLine += 1;
                        CkAggregateLineDTO matchedCkJoular = getMatchCkJoular(classMethodLine, ckAggregateLineHashMapDTO);
                        if (matchedCkJoular != null) {
                            nbAfterMatchedCkJoular += 1;
                            String classMethodSignature = getClassMethodSignature(nextLine);
                            addOrUpdateJoularEntityListDTO(matchedCkJoular, commitCompleteDTO, methodElementSetDTO, joularEntityDTOList, value, iterationDTO, classMethodSignature);
                        }
                    }
                }
            }
            log.debug("---------------------------------------");
            log.debug("size of allLines : {}", allLines.size());
            log.debug("nbAfterPatternMatching : {}", nbAfterPatternMatching);
            log.debug("nbAfterClassMethodLine : {}", nbAfterClassMethodLine);
            log.debug("nbAfterMatchedCkJoular : {}", nbAfterMatchedCkJoular);
            log.debug("---------------------------------------");
            //log.debug(String.valueOf(joularEntityDTOList.size()));
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
        return joularEntityDTOList;
    }

    private String getClassMethodSignature(String line) {
        return line.substring(0, line.lastIndexOf(" "));
    }

    private void addOrUpdateJoularEntityListDTO(CkAggregateLineDTO matchedCkJoular, CommitCompleteDTO commitCompleteDTO, MethodElementSetDTO methodElementSetDTO, JoularEntityListDTO joularEntityDTOList, Float value, IterationDTO iterationDTO, String classMethodSignature) {
        CommitSimpleDTO commitSimpleDTO = Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO);
        JoularEntityDTO joularEntityDTO = new JoularEntityDTO();
        MeasurableElementDTO methodElementDTO = getMeasurableElementForJoular(matchedCkJoular, classMethodSignature);
        if (methodElementSetDTO.has(methodElementDTO)){
            joularEntityDTOList.update(methodElementDTO, value);
        } else {
            methodElementSetDTO.add(methodElementDTO);
            joularEntityDTO.setIterationDTO(iterationDTO);
            joularEntityDTO.setCommitSimpleDTO(commitSimpleDTO);
            joularEntityDTO.setScope("app");
            joularEntityDTO.setMonitoringType("total");
            joularEntityDTO.setValue(value);
            joularEntityDTO.setMethodElementDTO(methodElementDTO);
            //log.debug("New : {}", joularEntityDTO.getMethodElementDTO().getClassMethodSignature());
            joularEntityDTOList.add(joularEntityDTO);
        }
    }

    public CkAggregateLineDTO getMatchCkJoular(JSONObject classMethodLine, CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO) {
        String className = Util.classNameParser(classMethodLine.getString(CLASS_NAME));
        String methodName = Util.methodNameParser(className, classMethodLine.getString(METHOD_NAME));
        int numberLine = classMethodLine.getInt(LINE_NUMBER);
        if (numberLine > 0) {
            List<CkAggregateLineDTO> allOccurrences = ckAggregateLineHashMapDTO.getAllOccurrences(className, methodName);
            if (!allOccurrences.isEmpty()) {
                for (CkAggregateLineDTO occ : allOccurrences) {
                    if (occ.getLine() <= numberLine && (occ.getLine() + occ.getLoc()) >= numberLine) {
                        //log.debug("Occurrence returned for \"{}.{}\" at line {}", className, methodName, numberLine);
                        return occ;
                    }
                }
            } else if (!methodName.contains("access$")) {
                log.debug("No occurrence for {} {} at line {}", className, methodName, numberLine);
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
