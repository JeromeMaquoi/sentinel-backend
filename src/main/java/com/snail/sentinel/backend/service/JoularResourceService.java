package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.service.dto.IterationDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MethodElementSetDTO;
import org.json.JSONObject;

import java.util.HashMap;

public interface JoularResourceService {
    void setCommitSimpleDTO(HashMap<String, String> repoItem, JSONObject commitData);
    void setCommitSimpleDTO(CommitSimpleDTO commitSimpleDTO);
    CommitSimpleDTO getCommitSimpleDTO();
    void setCkAggregateLineHashMapDTO(String repoName);
    void setCkAggregateLineHashMapDTO(CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO);
    CkAggregateLineHashMapDTO getCkAggregateLineHashMapDTO();
    void setIterationDTO(IterationDTO iterationDTO);
    IterationDTO getIterationDTO();
    void setFileListProvider(FileListProvider fileListProvider);
    FileListProvider getFileListProvider();
    void setMethodElementSetDTO(MethodElementSetDTO methodElementSetDTO);
    MethodElementSetDTO getMethodElementSetDTO();
    void setJoularEntityListDTO(JoularEntityListDTO joularEntityListDTO);
    JoularEntityListDTO getJoularEntityListDTO();
}