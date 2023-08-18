package com.snail.sentinel.backend.domain;

import com.snail.sentinel.backend.service.dto.RepositoryDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CommitEntity.
 */
@Document(collection = "commit_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommitEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("sha")
    private String sha;

    @NotNull
    @Field("repository")
    private transient RepositoryDTO repository;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CommitEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSha() {
        return this.sha;
    }

    public CommitEntity sha(String sha) {
        this.setSha(sha);
        return this;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public RepositoryDTO getRepository() {
        return repository;
    }

    public void setRepository(RepositoryDTO repository) {
        this.repository = repository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitEntity that = (CommitEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(sha, that.sha) && Objects.equals(repository, that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sha, repository);
    }

    @Override
    public String toString() {
        return "CommitEntity{" +
            "id='" + id + '\'' +
            ", sha='" + sha + '\'' +
            ", repository=" + repository +
            '}';
    }
}
