package com.snail.sentinel.backend.service.dto;

import jakarta.validation.constraints.NotNull;


public class CommitEntityDTO {
    @NotNull
    private String sha;

    @NotNull
    private String repoName;

    @NotNull
    private String owner;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
