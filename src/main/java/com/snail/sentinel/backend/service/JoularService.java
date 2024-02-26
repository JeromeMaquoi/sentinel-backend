package com.snail.sentinel.backend.service;

import org.json.JSONObject;

import java.util.HashMap;


public interface JoularService {
    void insertBatchJoularData(HashMap<String, String> repoItem, JSONObject commitData);
}
