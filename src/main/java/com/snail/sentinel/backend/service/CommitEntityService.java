package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.domain.CommitEntity;
import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import com.snail.sentinel.backend.service.dto.commit.CommitCompleteDTO;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;

public interface CommitEntityService {
    CommitEntity add(CommitCompleteDTO commitCompleteDTO);
    Optional<CommitEntity> findOneBySha(String sha);
    List<CommitEntity> bulkAdd(List<CommitCompleteDTO> listCommitDTO);
    void deleteAll();
    CommitCompleteDTO createCommitEntityDTO(RepoDataDTO repoItem, JSONObject commitData);
    JSONObject getCommitData(String owner, String repoName, String sha) throws Exception;
}
