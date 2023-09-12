package com.snail.sentinel.backend.service.dto.commit;

import com.snail.sentinel.backend.service.dto.repository.RepositorySimpleDTO;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CommitSimpleDTO {
    @NotNull
    private String sha;

    @NotNull
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitSimpleDTO that = (CommitSimpleDTO) o;
        return Objects.equals(sha, that.sha) && Objects.equals(repository, that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sha, repository);
    }

    @Override
    public String toString() {
        return "CommitSimpleDTO{" +
            "sha='" + sha + '\'' +
            ", repositorySimpleDTO=" + repository +
            '}';
    }
}
