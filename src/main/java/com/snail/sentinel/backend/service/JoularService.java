package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.commons.FileListProvider;
import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;

import java.util.List;

public interface JoularService {
    List<JoularEntityDTO> findAll();

    List<JoularEntityDTO> bulkAdd(List<JoularEntityDTO> listJoular);

    void deleteAll();

    List<JoularEntity> findByCommitSha(String sha);

    List<JoularEntity> findByCommitShaAndAstElement(String sha, String className, String methodSignature);

    void setCkAggregateLineHashMapDTO(String repoName);

    JoularEntityListDTO createJoularEntityDTOList(CommitCompleteDTO commitCompleteDTO, String iterationPath, FileListProvider fileListProvider);
}
