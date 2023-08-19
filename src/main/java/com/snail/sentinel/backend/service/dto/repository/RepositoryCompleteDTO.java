package com.snail.sentinel.backend.service.dto.repository;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RepositoryCompleteDTO {
    @NotNull
    private String name;

    @NotNull
    private String owner;

    @NotNull
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepositoryCompleteDTO that = (RepositoryCompleteDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(owner, that.owner) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner, url);
    }

    @Override
    public String toString() {
        return "RepositoryCompleteDTO{" +
            "name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            ", url='" + url + '\'' +
            '}';
    }
}
