package com.snail.sentinel.backend.domain;

import com.snail.sentinel.backend.service.dto.commit.StatsDTO;
import com.snail.sentinel.backend.service.dto.repository.RepositoryCompleteDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
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
    @Field("date")
    private String date;

    @NotNull
    @Field("message")
    private String message;

    @NotNull
    @Field("parents_sha")
    private List<String> parentsSha;

    @NotNull
    @Field("repository")
    private transient RepositoryCompleteDTO repository;

    @NotNull
    @Field("stats")
    private transient StatsDTO stats;

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

    public RepositoryCompleteDTO getRepository() {
        return repository;
    }

    public void setRepository(RepositoryCompleteDTO repository) {
        this.repository = repository;
    }

    public StatsDTO getStats() {
        return stats;
    }

    public void setStats(StatsDTO stats) {
        this.stats = stats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommitEntity that = (CommitEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(sha, that.sha) && Objects.equals(date, that.date) && Objects.equals(message, that.message) && Objects.equals(parentsSha, that.parentsSha) && Objects.equals(repository, that.repository) && Objects.equals(stats, that.stats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sha, date, message, parentsSha, repository, stats);
    }

    @Override
    public String toString() {
        return "CommitEntity{" +
            "id='" + id + '\'' +
            ", sha='" + sha + '\'' +
            ", date='" + date + '\'' +
            ", message='" + message + '\'' +
            ", parentsSha=" + parentsSha +
            ", repository=" + repository +
            ", stats=" + stats +
            '}';
    }
}
