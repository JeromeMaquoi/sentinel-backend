package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CkEntity;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;

import java.io.IOException;
import java.util.List;

public interface CkEntityService {
    List<CkEntityDTO> findAll();

    List<CkEntityDTO> bulkAdd(List<CkEntityDTO> listCk);

    void deleteAll();

    List<CkEntity> findByCommitSha(String sha);

    List<CkEntity> findByCommitShaAndMetricName(String sha, String metricName);

    List<CkEntity> findByCommitShaAndMethodElementAndMetricNames(String sha, String className, String methodName, List<String> names);

    void insertBatchCkEntityDTO(CommitCompleteDTO commitCompleteDTO, String csvPath, int batchSize) throws IOException;
}
