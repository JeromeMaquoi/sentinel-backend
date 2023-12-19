package com.snail.sentinel.backend.repository;

import com.snail.sentinel.backend.service.dto.joular.JoularAggregateDTO;

import java.util.List;

public interface JoularEntityRepositoryAggregation {
    List<JoularAggregateDTO> aggregateAll();
}
