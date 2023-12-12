package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityListDTO;

import java.util.List;

public interface JoularEntityService {
    List<JoularEntityDTO> findAll();

    List<JoularEntityDTO> bulkAdd(List<JoularEntityDTO> listJoular);

    void deleteAll();

    List<JoularEntity> findByCommitSha(String sha);

    List<JoularEntity> findByCommitShaAndAstElement(String sha, String className, String methodSignature);

    JoularEntityListDTO createJoularEntityDTOList(CkAggregateLineHashMapDTO ckAggregateLineHashMapDTO, CommitCompleteDTO commitCompleteDTO, String iterationPath);
}
