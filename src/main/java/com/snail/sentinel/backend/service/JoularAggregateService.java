package com.snail.sentinel.backend.service;


import com.snail.sentinel.backend.service.dto.joular.JoularAggregateDTO;

import java.util.List;

public interface JoularAggregateService {
    List<JoularAggregateDTO> aggregateAll();
    List<JoularAggregateDTO> aggregateAllByCommit(String sha);
}
