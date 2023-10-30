package com.snail.sentinel.backend.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class IterationDTO {
    @NotNull
    private Integer iterationId;

    @NotNull
    private Integer pid;

    @NotNull
    private long startTimestamp;

    public IterationDTO() {}

    public IterationDTO(Integer iterationId, Integer pid, long startTimestamp) {
        this.iterationId = iterationId;
        this.pid = pid;
        this.startTimestamp = startTimestamp;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IterationDTO that = (IterationDTO) o;
        return startTimestamp == that.startTimestamp && Objects.equals(iterationId, that.iterationId) && Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iterationId, pid, startTimestamp);
    }

    @Override
    public String toString() {
        return "IterationDTO{" +
            "iterationId=" + iterationId +
            ", pid=" + pid +
            ", startTimestamp=" + startTimestamp +
            '}';
    }
}
