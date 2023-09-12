package com.snail.sentinel.backend.service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class IterationDTO {
    @NotNull
    private Integer pid;

    @NotNull
    private long startTimestamp;

    public IterationDTO() {}

    public IterationDTO(Integer pid, long startTimestamp) {
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
        return Objects.equals(pid, that.pid) && Objects.equals(startTimestamp, that.startTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, startTimestamp);
    }

    @Override
    public String toString() {
        return "IterationDTO{" +
            "pid=" + pid +
            ", startTimestamp=" + startTimestamp +
            '}';
    }
}
