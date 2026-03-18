package com.snail.sentinel.backend.service.dto.aggregation;

import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;

public class CommitAggregateDTO {
    private String sha;
    private RepositorySimpleDTO repository;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public RepositorySimpleDTO getRepository() {
        return repository;
    }

    public void setRepository(RepositorySimpleDTO repository) {
        this.repository = repository;
    }

    @Override
    public String toString() {
        return "CommitAggregateDTO{" +
            "sha='" + sha + '\'' +
            ", repository=" + repository +
            '}';
    }
}
