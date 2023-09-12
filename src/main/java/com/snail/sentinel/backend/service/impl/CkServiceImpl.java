package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.repository.CkEntityRepository;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregationImpl;
import com.snail.sentinel.backend.service.CkEntityService;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import com.snail.sentinel.backend.service.mapper.CkEntityMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.snail.sentinel.backend.commons.Util.getMeasurableElement;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

@Service
public class CkServiceImpl implements CkEntityService {
    private final Logger log = LoggerFactory.getLogger(CkServiceImpl.class);

    private final CkEntityRepository ckEntityRepository;

    private final CkEntityMapper ckEntityMapper;

    private final CkEntityRepositoryAggregationImpl ckEntityRepositoryAggregationImpl;

    public CkServiceImpl(CkEntityRepository ckEntityRepository, CkEntityMapper ckEntityMapper, CkEntityRepositoryAggregationImpl ckEntityRepositoryAggregationImpl) {
        this.ckEntityRepository = ckEntityRepository;
        this.ckEntityMapper = ckEntityMapper;
        this.ckEntityRepositoryAggregationImpl = ckEntityRepositoryAggregationImpl;
    }

    @Override
    public List<CkEntityDTO> findAll() {
        return ckEntityRepository.findAll().stream().map(ckEntityMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CkEntityDTO> bulkAdd(List<CkEntityDTO> listCk) {
        log.info("Request to save a list of CkEntity");
        List<CkEntity> listEntity = ckEntityMapper.toEntity(listCk);
        listEntity = ckEntityRepository.insert(listEntity);
        return ckEntityMapper.toDto(listEntity);
    }

    @Override
    public void deleteAll() {
        ckEntityRepository.deleteAll();
    }

    @Override
    public CkAggregateLineHashMapDTO aggregate(String repoName) {
        return ckEntityRepositoryAggregationImpl.aggregate(repoName);
    }

    public CkEntity buildNewCkEntity(CkEntityDTO ckEntityDTO) {
        CkEntity newCk = new CkEntity();
        newCk.setName(ckEntityDTO.getName());
        newCk.setValue(ckEntityDTO.getValue());
        newCk.setToolVersion(ckEntityDTO.getToolVersion());
        newCk.setCommit(ckEntityDTO.getCommit());
        newCk.setMeasurableElement(ckEntityDTO.getMeasurableElementDTO());
        log.debug("Created Information for Ck: {}", newCk);
        return newCk;
    }

    public List<CkEntityDTO> createCkEntityDTOList(Map<String, String> repoItem, CommitCompleteDTO commitCompleteDTO) throws IOException {
        List<CkEntityDTO> listCkEntityDTO = new ArrayList<>();
        String csvPath = System.getenv("REPO_PATH") + repoItem.get(Util.NAME) + "/output-ck/";
        for (String astElem: Arrays.asList(Util.AST_ELEM_CLASS, Util.AST_ELEM_METHOD, Util.AST_ELEM_VARIABLE)) {
            String csvCkPath = csvPath + astElem + ".csv";
            List<JSONObject> allLines = Util.readCsvToJson(csvCkPath);
            for (JSONObject line: allLines) {
                for (String metric: line.keySet()) {
                    if (!Arrays.asList(Util.FILE, Util.AST_ELEM_CLASS, Util.AST_ELEM_METHOD, "type", Util.AST_ELEM_VARIABLE).contains(metric)) {
                        CkEntityDTO ckEntityDTO = createCkEntityDTOForList(astElem, line, commitCompleteDTO, metric);
                        listCkEntityDTO.add(ckEntityDTO);
                    }
                }
            }
        }
        log.info("List of CkEntityDTO created");
        return listCkEntityDTO;
    }

    public CkEntityDTO createCkEntityDTOForList(String astElem, JSONObject line, CommitCompleteDTO commitCompleteDTO, String metric) {
        MeasurableElementDTO measurableElementDTO = getMeasurableElement(astElem, line);

        CkEntityDTO ckEntityDTO = new CkEntityDTO();
        ckEntityDTO.setCommit(Util.createCommitSimpleFromCommitCompleteDTO(commitCompleteDTO));
        ckEntityDTO.setToolVersion(Util.TOOL_VERSION);
        ckEntityDTO.setMeasurableElementDTO(measurableElementDTO);
        ckEntityDTO.setName(metric);

        if (line.getString(metric).equals("false") || line.getString(metric).equals("true")) {
            ckEntityDTO.setValue(parseBoolean(line.getString(metric)));
        } else {
            ckEntityDTO.setValue(parseDouble(line.getString(metric)));
        }
        return ckEntityDTO;
    }
}
