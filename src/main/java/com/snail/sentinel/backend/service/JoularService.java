package com.snail.sentinel.backend.service;

import java.util.HashMap;


public interface JoularService {
    void setCkAggregateLineHashMapDTO(String repoName);
    void insertBatchJoularData(HashMap<String, String> repoItem);
}
