package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.CommitEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularNodeEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementSetDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

@Service
public class JoularResourceServiceImpl implements JoularResourceService {
    private final Logger log = LoggerFactory.getLogger(JoularServiceImpl.class);

    private final CommitEntityService commitEntityService;

    private final CkEntityRepositoryAggregation ckEntityRepositoryAggregation;

    private CommitSimpleDTO commitSimpleDTO;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private IterationDTO iterationDTO;

    private JoularEntityListDTO joularEntityListDTO;

    private JoularNodeEntityListDTO joularNodeEntityListDTO;

    private FileListProvider fileListProvider;

    private MethodElementSetDTO methodElementSetDTO;

    private List<String> ancestors;

    private static final String CLASS_NAME = "className";

    private static final String METHOD_NAME = "methodName";

    private static final String LINE_NUMBER = "lineNumber";

    public JoularResourceServiceImpl(CommitEntityService commitEntityService, CkEntityRepositoryAggregation ckEntityRepositoryAggregation) {
        this.commitEntityService = commitEntityService;
        this.ckEntityRepositoryAggregation = ckEntityRepositoryAggregation;
    }

    @Override
    public void setCommitSimpleDTO(HashMap<String, String> repoItem, JSONObject commitData) {
        CommitCompleteDTO commitCompleteDTO = commitEntityService.createCommitEntityDTO(repoItem, commitData);
        this.commitSimpleDTO = Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO);
    }

    @Override
    public void setCommitSimpleDTO(CommitSimpleDTO commitSimpleDTO) {
        this.commitSimpleDTO = commitSimpleDTO;
    }

    @Override
    public CommitSimpleDTO getCommitSimpleDTO() {
        return this.commitSimpleDTO;
    }

    @Override
    public void setCkAggregateLineHashMapDTO(String repoName) {
        this.ckAggregateLineHashMapDTO = ckEntityRepositoryAggregation.aggregate(repoName);
    }

    @Override
    public void setCkAggregateLineHashMapDTO(CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO) {
        this.ckAggregateLineHashMapDTO = ckAggregateLineHashMapDTO;
    }

    @Override
    public CkAggregateLineHashMapDTO getCkAggregateLineHashMapDTO() {
        return this.ckAggregateLineHashMapDTO;
    }

    @Override
    public CkAggregateLineDTO getMatchCkJoular(String classMethodLineString) {
        Optional<JSONObject> optionalResult = getClassMethodLine(classMethodLineString);
        if (optionalResult.isPresent()) {
            JSONObject classMethodLine = optionalResult.get();
            String className = Util.classNameParser(classMethodLine.getString(CLASS_NAME));
            String methodName = Util.methodNameParser(className, classMethodLine.getString(METHOD_NAME));
            int numberLine = classMethodLine.getInt(LINE_NUMBER);
            if (numberLine > 0) {
                List<CkAggregateLineDTO> allOccurrences = getCkAggregateLineHashMapDTO().getAllOccurrences(className, methodName);
                if (!allOccurrences.isEmpty()) {
                    for (CkAggregateLineDTO occ : allOccurrences) {
                        if (occ.getLine() <= numberLine && (occ.getLine() + occ.getLoc()) >= numberLine) {
                            return occ;
                        }
                    }
                } else if (!methodName.contains("access$")) {
                    //log.debug("No occurrence for {} {} at line {}", className, methodName, numberLine);
                }
            } /*else {
            log.warn("The number of line is negative for \"{}.{}\"", className, methodName);
        }*/
        }
        return null;
    }

    public Optional<JSONObject> getClassMethodLine(String metric) {
        String className = metric.substring(0, metric.lastIndexOf('.'));
        String[] spaceSplit = metric.substring(metric.lastIndexOf('.') + 1).split(" ");
        if (spaceSplit.length < 3) {
            String methodName = spaceSplit[0];
            int numberLine = parseInt(spaceSplit[1]);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CLASS_NAME, className);
            jsonObject.put(METHOD_NAME, methodName);
            jsonObject.put(LINE_NUMBER, numberLine);
            return Optional.of(jsonObject);
        }
        return Optional.empty();
    }

    @Override
    public void setIterationDTO(IterationDTO iterationDTO) {
        this.iterationDTO = iterationDTO;
    }

    @Override
    public IterationDTO getIterationDTO() {
        return this.iterationDTO;
    }

    @Override
    public void setFileListProvider(FileListProvider fileListProvider) {
        this.fileListProvider = fileListProvider;
    }

    @Override
    public FileListProvider getFileListProvider() {
        return this.fileListProvider;
    }

    @Override
    public void setMethodElementSetDTO(MethodElementSetDTO methodElementSetDTO) {
        this.methodElementSetDTO = methodElementSetDTO;
    }

    @Override
    public MethodElementSetDTO getMethodElementSetDTO() {
        return this.methodElementSetDTO;
    }

    @Override
    public void setJoularEntityListDTO(JoularEntityListDTO joularEntityListDTO) {
        this.joularEntityListDTO = joularEntityListDTO;
    }

    @Override
    public JoularEntityListDTO getJoularEntityListDTO() {
        return this.joularEntityListDTO;
    }

    @Override
    public void setJoularNodeEntityListDTO(JoularNodeEntityListDTO joularNodeEntityListDTO) {
        this.joularNodeEntityListDTO = joularNodeEntityListDTO;
    }

    @Override
    public JoularNodeEntityListDTO getJoularNodeEntityListDTO() {
        return this.joularNodeEntityListDTO;
    }

    @Override
    public void setAncestors(List<String> ancestors) {
        this.ancestors = ancestors;
    }

    @Override
    public List<String> getAncestors() {
        return this.ancestors;
    }
}
