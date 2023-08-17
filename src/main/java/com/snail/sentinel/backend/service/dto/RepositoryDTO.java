package com.snail.sentinel.backend.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class RepositoryDTO {
    @NotNull
    private String name;

    @NotNull
    private String owner;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepositoryDTO that = (RepositoryDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, owner);
    }

    @Override
    public String toString() {
        return "RepositoryDTO{" +
            "name='" + name + '\'' +
            ", owner='" + owner + '\'' +
            '}';
    }
}
