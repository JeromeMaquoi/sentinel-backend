package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.RepoDataDTO;
import org.json.JSONObject;


public interface JoularService {
    void insertBatchJoularData(RepoDataDTO repoItem, JSONObject commitData);
}
