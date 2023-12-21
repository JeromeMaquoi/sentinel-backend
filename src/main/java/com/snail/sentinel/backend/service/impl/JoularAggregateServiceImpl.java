package com.snail.sentinel.backend.service.impl;

import com.snail.sentinel.backend.repository.JoularEntityRepositoryAggregation;
import com.snail.sentinel.backend.service.JoularAggregateService;
import com.snail.sentinel.backend.service.dto.joular.JoularAggregateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoularAggregateServiceImpl implements JoularAggregateService {
    private final JoularEntityRepositoryAggregation joularEntityRepositoryAggregation;

    public JoularAggregateServiceImpl(JoularEntityRepositoryAggregation joularEntityRepositoryAggregation) {
        this.joularEntityRepositoryAggregation = joularEntityRepositoryAggregation;
    }

    @Override
    public List<JoularAggregateDTO> aggregateAll() {
        return joularEntityRepositoryAggregation.aggregateAll();
    }

    @Override
    public List<JoularAggregateDTO> aggregateAllByCommit(String sha) {
        return joularEntityRepositoryAggregation.aggregateAllByCommit(sha);
    }
}
