package com.snail.sentinel.backend.service.dto.commit;

import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class CommitCompleteDTO {
    @NotNull
    private String sha;

    @NotNull
    private String date;

    @NotNull
    private String message;

    @NotNull
    private List<String> parentsSha;

    @NotNull
    private RepositoryCompleteDTO repositoryCompleteDTO;

    @NotNull
    private StatsDTO statsDTO;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getParentsSha() {
        return parentsSha;
    }

    public void setParentsSha(List<String> parentsSha) {
        this.parentsSha = parentsSha;
    }

    public RepositoryCompleteDTO getRepositoryCompleteDTO() {
        return repositoryCompleteDTO;
    }

    public void setRepositoryCompleteDTO(RepositoryCompleteDTO repositoryCompleteDTO) {
        this.repositoryCompleteDTO = repositoryCompleteDTO;
    }

    public StatsDTO getStatsDTO() {
        return statsDTO;
    }

    public void setStatsDTO(StatsDTO statsDTO) {
        this.statsDTO = statsDTO;
    }

    @Override
    public String toString() {
        return "CommitEntityDTO{" +
            "sha='" + sha + '\'' +
            ", date='" + date + '\'' +
            ", message='" + message + '\'' +
            ", parentsSha=" + parentsSha +
            ", repositoryCompleteDTO=" + repositoryCompleteDTO +
            ", statsDTO=" + statsDTO +
            '}';
    }
}
