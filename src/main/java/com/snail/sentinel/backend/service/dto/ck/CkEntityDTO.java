package com.snail.sentinel.backend.service.dto.ck;

import com.snail.sentinel.backend.service.dto.commit.CommitSimpleDTO;
import com.snail.sentinel.backend.service.dto.measurableelement.MeasurableElementDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.snail.sentinel.backend.domain.CkEntity} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CkEntityDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private  transient Object value;

    @NotNull
    private String toolVersion;

    @NotNull
    private transient CommitSimpleDTO commit;

    @NotNull
    private transient MeasurableElementDTO measurableElementDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(String toolVersion) {
        this.toolVersion = toolVersion;
    }

    public CommitSimpleDTO getCommit() {
        return commit;
    }

    public void setCommit(CommitSimpleDTO commit) {
        this.commit = commit;
    }

    public MeasurableElementDTO getMeasurableElementDTO() {
        return measurableElementDTO;
    }

    public void setMeasurableElementDTO(MeasurableElementDTO measurableElementDTO) {
        this.measurableElementDTO = measurableElementDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CkEntityDTO that = (CkEntityDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(toolVersion, that.toolVersion) && Objects.equals(commit, that.commit) && Objects.equals(measurableElementDTO, that.measurableElementDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, toolVersion, commit, measurableElementDTO);
    }

    @Override
    public String toString() {
        return "CkEntityDTO{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", value=" + value +
            ", toolVersion='" + toolVersion + '\'' +
            ", commit=" + commit +
            ", measurableElementDTO=" + measurableElementDTO +
            '}';
    }
}
