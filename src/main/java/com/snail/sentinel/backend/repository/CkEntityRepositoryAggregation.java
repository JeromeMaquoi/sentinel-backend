package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineDTO;
import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;

import java.util.List;


public interface CkEntityRepositoryAggregation {

    CkAggregateLineHashMapDTO aggregate(String repoName);
    List<CkAggregateLineDTO> aggregateClassMethod(String repoName, String className, String methodName);
}
