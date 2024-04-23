package com.snail.sentinel.backend.service.dto;

import com.snail.sentinel.backend.commons.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RepoDataDTO {
    private Map<String, String> oneRepoData;

    public RepoDataDTO(String owner, String name, String sha) {
        oneRepoData = new HashMap<>();
        oneRepoData.put(Util.OWNER, owner);
        oneRepoData.put(Util.NAME, name);
        oneRepoData.put(Util.SHA, sha);
    }

    public String getOwner() {
        return oneRepoData.get(Util.OWNER);
    }

    public String getName() {
        return oneRepoData.get(Util.NAME);
    }

    public String getSha() {
        return oneRepoData.get(Util.SHA);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepoDataDTO that = (RepoDataDTO) o;
        return Objects.equals(oneRepoData, that.oneRepoData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(oneRepoData);
    }

    @Override
    public String toString() {
        return "RepoDataDTO{" +
            "oneRepoData=" + oneRepoData +
            '}';
    }
}
