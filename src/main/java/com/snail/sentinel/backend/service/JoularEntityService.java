package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.JoularEntity;
import com.snail.sentinel.backend.service.dto.joular.JoularEntityDTO;

import java.nio.file.Path;
import java.util.List;

public interface JoularEntityService {
    List<JoularEntityDTO> findAll();

    List<JoularEntityDTO> bulkAdd(List<JoularEntityDTO> listJoular);

    void deleteAll();

    List<JoularEntity> findByCommitSha(String sha);

    int countByCommitSha(String sha);

    List<JoularEntity> findByCommitShaAndAstElement(String sha, String className, String methodSignature);

    void handleJoularEntityCreationForOneIteration(Path iterationFilePath);
}
