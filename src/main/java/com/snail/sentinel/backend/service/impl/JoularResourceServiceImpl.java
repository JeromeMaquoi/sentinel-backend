package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.commons.Util;
import com.snail.sentinel.backend.repository.CkEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.CommitEntityService;
import com.snail.sentinel.backend.service.JoularResourceService;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementSetDTO;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class JoularResourceServiceImpl implements JoularResourceService {
    private final CommitEntityService commitEntityService;

    private final CkEntityRepositoryAggregation ckEntityRepositoryAggregation;

    private CommitSimpleDTO commitSimpleDTO;

    private CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO;

    private IterationDTO iterationDTO;

    private JoularEntityListDTO joularEntityListDTO;

    private FileListProvider fileListProvider;

    private MethodElementSetDTO methodElementSetDTO;

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
}
