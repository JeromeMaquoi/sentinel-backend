package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.JoularNodeEntity;
import com.snail.sentinel.backend.repository.JoularNodeEntityRepository;
import com.snail.sentinel.backend.service.JoularNodeEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.JoularNodeEntityDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeHashMapDTO;
import com.snail.sentinel.backend.service.dto.joularnode.JoularNodeKeyHashMap;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.exceptions.CreateJoularNodeEntityMeasurableElementException;
import com.snail.sentinel.backend.service.exceptions.GetMatchCkJoularException;
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

    private int numberOfMethods;

    private int numberOfUnhandledMethods;

    private boolean ignoreLine = false;

    // List of all the methods of the current line
    List<JoularNodeEntityDTO> methodsOfCurrentLine;

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
    public void deleteByCommitSha(String commitSha) {
        joularNodeEntityRepository.deleteByCommitSha(commitSha);
    }

    @Override
    public int countByCommitSha(String sha) {
        log.info("Count number of JoularNodeEntities by commit sha : {} = {}", sha, joularNodeEntityRepository.countByCommitSha(sha));
        return joularNodeEntityRepository.countByCommitSha(sha);
    }

    @Override
    public void handleJoularNodeEntityCreationForOneIteration(Path iterationFilePath) {
        log.info("\n");
        log.info("\n");
        log.info("\n");
        log.info("Request to handle JoularNodeEntity for iteration {}", iterationFilePath);
        setNumberOfMethods(0);
        setNumberOfUnhandledMethods(0);
        this.joularNodeHashMapDTO = new JoularNodeHashMapDTO();
        createJoularNodeEntityDTOList(iterationFilePath);
        bulkAdd(joularResourceService.getJoularNodeEntityListDTO().getList());
        log.info("Total number of cells in the csv : {}", getNumberOfMethods());
        log.info("Number of unhandled cells in the csv : {}", getNumberOfUnhandledMethods());
    }

    public void createJoularNodeEntityDTOList(Path iterationFilePath) {
        log.debug("iterationFilePath : {}", iterationFilePath);
        joularResourceService.setJoularNodeEntityListDTO(new JoularNodeEntityListDTO());
        try {
            Path csvpath = joularResourceService.getFileListProvider().getFileList(iterationFilePath + "/app/total/calltrees").iterator().next();
            List<JSONObject> allLines = Util.readCsvWithoutHeaderToJson(csvpath.toString());
            int lineNumber = 1;
            for (JSONObject line : allLines) {
                handleOneCsvLine(lineNumber, line);
                lineNumber++;
            }
        } catch(NoSuchElementException e) {
            log.warn("No file found for the given iteration path : {}", iterationFilePath);
        } catch (IOException e) {
            throw new NoCsvLineFoundException(e);
        }
    }

    public void handleOneCsvLine(int lineNumber, JSONObject line) {
        log.debug("--------");
        log.debug("New line: {}", line);
        String nextLine = line.keySet().iterator().next();
        Float value = line.getFloat(nextLine);
        List<String> allLineNodes = getEachNodeFromStringLine(nextLine);

        // List of all ancestors of one method of the line
        joularResourceService.setAncestors(new ArrayList<>());
        setMethodsOfCurrentLine();
        setIgnoreLine(false);

        log.debug("ancestors list reset");
        // Check if last element of array
        for (String methodNameAndLine : allLineNodes) {
            setLastElement(allLineNodes.indexOf(methodNameAndLine) == allLineNodes.size() - 1);
            // For now, the length is limited to 2 because it prevents from the kotlin source code to be inserted, because these stack trace elements contain more than one "," so the app is not designed to handle this, yet
            //TODO handle Kotlin source code
            if (methodNameAndLine.split(" ").length == 2 && !ignoreLine) {
                handleOneMethodFromOneCsvLine(methodNameAndLine, value);
            } else {
                addMethod();
                addUnhandledMethod();
                setIgnoreLine(true);
            }
        }

        if (ignoreLine) {
            //log.warn("{} : {}", lineNumber, nextLine);
            joularResourceService.getLoggingToFile().writeTimeToFileUnhandledMethods("Line " + lineNumber + ": " + nextLine);
            joularResourceService.getLoggingToFile().writeEmptyLineToFile();
        } else {
            joularResourceService.getJoularNodeEntityListDTO().addAll(methodsOfCurrentLine);
        }
    }

    public void handleOneMethodFromOneCsvLine(String classMethodLineString, Float value) {
        assert this.joularNodeHashMapDTO != null : "joularNodeHashMapDTO is null";
        assert joularResourceService.getJoularNodeEntityListDTO() != null : "getJoularNodeEntityListDTO() returns null";
        assert this.methodsOfCurrentLine != null : "methodsOfCurrentLine is null";
        assert joularResourceService.getLoggingToFile() != null : "joularResourceService.getLoggingToFile() is null";

        int lineNumber = Integer.parseInt(classMethodLineString.split(" ")[1]);
        addMethod();

        JoularNodeKeyHashMap key = new JoularNodeKeyHashMap(classMethodLineString.split(" ")[0], lineNumber, joularResourceService.getAncestors());

        if (!this.joularNodeHashMapDTO.isJoularNodeEntityDTOInMap(key) || isLastElement()) {
            Optional<MeasurableElementDTO> optionalMeasurableElementDTO = createJoularNodeEntityMeasurableElement(classMethodLineString);

            if (optionalMeasurableElementDTO.isPresent() && lineNumber > 0) {
                MeasurableElementDTO measurableElementDTO = optionalMeasurableElementDTO.orElseThrow(() -> new CreateJoularNodeEntityMeasurableElementException(classMethodLineString));
                joularNodeEntityDTO = populateJoularNodeEntityDTO(measurableElementDTO, lineNumber, value);
                log.debug("ancestors for {} : {}", joularNodeEntityDTO.getMeasurableElement().getMethodName(), joularNodeEntityDTO.getAncestors());
                this.joularNodeHashMapDTO.insertOne(joularNodeEntityDTO);
                joularResourceService.getAncestors().add(joularNodeEntityDTO.getId());
                log.debug("ancestors for next cell : {}", joularResourceService.getAncestors());
                methodsOfCurrentLine.add(joularNodeEntityDTO);

            } else if (lineNumber > 0 && !classMethodLineString.contains("<clinit>") && !classMethodLineString.contains("<init>") && !classMethodLineString.contains("access$000") && !classMethodLineString.contains("$")){
                //log.warn("{} : No JoularNodeEntity set for {}", getNumberOfMethods(), classMethodLineString);
                joularResourceService.getLoggingToFile().writeTimeToFileForWarningIterationResult(getNumberOfMethods(), "No JoularNodeEntity set for " +  classMethodLineString);
                addUnhandledMethod();
                setIgnoreLine(true);
            }
        } else {
            log.debug("JoularNodeEntityDTO {} already in map. Adding its id to the ancestors list", classMethodLineString);
            String id = this.joularNodeHashMapDTO.getJoularNodeEntityDTO(key).getId();
            joularResourceService.getAncestors().add(id);
        }
    }

    public JoularNodeEntityDTO populateJoularNodeEntityDTO(MeasurableElementDTO measurableElementDTO, int lineNumber, Float value) {
        JoularNodeEntityDTO localJoularNodeEntityDTO = new JoularNodeEntityDTO();
        localJoularNodeEntityDTO.setMeasurableElement(measurableElementDTO);
        localJoularNodeEntityDTO.setId(UUID.randomUUID().toString());
        localJoularNodeEntityDTO.setLineNumber(lineNumber);
        localJoularNodeEntityDTO.setScope("app");
        localJoularNodeEntityDTO.setMonitoringType("calltrees");
        localJoularNodeEntityDTO.setIteration(joularResourceService.getIterationDTO());
        localJoularNodeEntityDTO.setCommit(joularResourceService.getCommitSimpleDTO());
        List<String> ancestors = new ArrayList<>(joularResourceService.getAncestors());
        localJoularNodeEntityDTO.setAncestors(ancestors);
        localJoularNodeEntityDTO.setParent(getParentFromAncestors());
        if (isLastElement()) {
            localJoularNodeEntityDTO.setValue(value);
        }
        return localJoularNodeEntityDTO;
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
            CkAggregateLineDTO matchedCkJoular = optionalMatchedCkJoular.orElseThrow(() -> new GetMatchCkJoularException(classMethodLineString));
            String classMethodSignature = classMethodLineString.substring(0, classMethodLineString.lastIndexOf(" "));
            return Optional.of(Util.getMeasurableElementForJoular(matchedCkJoular, classMethodSignature));
        }
        log.debug("createJoularNodeEntityMeasurableElement empty for {}", classMethodLineString);
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

    public void setJoularNodeEntityDTO(JoularNodeEntityDTO joularNodeEntityDTO) {
        this.joularNodeEntityDTO = joularNodeEntityDTO;
    }

    public JoularNodeEntityDTO getJoularNodeEntityDTO() {
        return joularNodeEntityDTO;
    }

    public void addMethod() {
        numberOfMethods++;
    }

    @Override
    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int number) {
        numberOfMethods = number;
    }

    public void addUnhandledMethod() {
        numberOfUnhandledMethods++;
    }

    @Override
    public int getNumberOfUnhandledMethods() {
        return numberOfUnhandledMethods;
    }

    public void setNumberOfUnhandledMethods(int number) {
        numberOfUnhandledMethods = number;
    }

    public void setMethodsOfCurrentLine() {
        methodsOfCurrentLine = new ArrayList<>();
    }

    @Override
    public List<JoularNodeEntityDTO> getMethodsOfCurrentLine() {
        return methodsOfCurrentLine;
    }

    public void setIgnoreLine(boolean ignoreLine) {
        this.ignoreLine = ignoreLine;
    }

    @Override
    public boolean getIgnoreLine() {
        return ignoreLine;
    }
}
