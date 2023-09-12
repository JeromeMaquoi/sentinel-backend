package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;


public interface CkEntityRepositoryAggregation {

    CkAggregateLineHashMapDTO aggregate(String repoName);
}
