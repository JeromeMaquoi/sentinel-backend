package com.snail.sentinel.backend.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CommitEntityDTO {
    @NotNull
    private String sha;

    @NotNull
    private RepositoryDTO repositoryDTO;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public RepositoryDTO getRepositoryDTO() {
        return repositoryDTO;
    }

    public void setRepositoryDTO(RepositoryDTO repositoryDTO) {
        this.repositoryDTO = repositoryDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitEntityDTO commitEntityDTO = (CommitEntityDTO) o;
        return Objects.equals(sha, commitEntityDTO.sha) && Objects.equals(repositoryDTO, commitEntityDTO.repositoryDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sha, repositoryDTO);
    }

    @Override
    public String toString() {
        return "commitDTO{" +
            "sha='" + sha + '\'' +
            ", repository=" + repositoryDTO +
            '}';
    }
}
