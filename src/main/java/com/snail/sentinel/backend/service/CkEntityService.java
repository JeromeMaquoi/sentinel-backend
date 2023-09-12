package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.ck.CkAggregateLineHashMapDTO;
import com.snail.sentinel.backend.service.dto.ck.CkEntityDTO;

import java.util.List;

public interface CkEntityService {
    List<CkEntityDTO> findAll();

    List<CkEntityDTO> bulkAdd(List<CkEntityDTO> listCk);

    void deleteAll();

    CkAggregateLineHashMapDTO aggregate(String repoName);
}
