package com.snail.sentinel.backend.domain;

import com.snail.sentinel.backend.service.dto.CommitDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CkEntity.
 */
@Document(collection = "ck_entity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CkEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("value")
    private Integer value;

    @NotNull
    @Field("tool_version")
    private String toolVersion;

    @NotNull
    @Field("commit")
    private transient CommitDTO commit;

    @NotNull
    @Field("measurable_element")
    private transient MeasurableElementDTO measurableElement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CkEntity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CkEntity name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return this.value;
    }

    public CkEntity value(Integer value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getToolVersion() {
        return this.toolVersion;
    }

    public CkEntity toolVersion(String toolVersion) {
        this.setToolVersion(toolVersion);
        return this;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public CommitDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitDTO commit) {
        this.commit = commit;
    }

    public MeasurableElementDTO getMeasurableElement() {
        return measurableElement;
    }

    public void setMeasurableElement(MeasurableElementDTO measurableElement) {
        this.measurableElement = measurableElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CkEntity ckEntity = (CkEntity) o;
        return Objects.equals(id, ckEntity.id) && Objects.equals(name, ckEntity.name) && Objects.equals(value, ckEntity.value) && Objects.equals(toolVersion, ckEntity.toolVersion) && Objects.equals(commit, ckEntity.commit) && Objects.equals(measurableElement, ckEntity.measurableElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, toolVersion, commit, measurableElement);
    }

    @Override
    public String toString() {
        return "CkEntity{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", value=" + value +
            ", toolVersion='" + toolVersion + '\'' +
            ", commit=" + commit +
            ", measurableElement=" + measurableElement +
            '}';
    }
}
